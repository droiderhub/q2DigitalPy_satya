package com.tarang.dpq2.worker;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.model.DeviceSpecificModel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/* ******** SSL Connection Factory ******* */
public class SSLSocketFactoryExtended extends SSLSocketFactory {
    private final boolean safView;
    private SSLContext mSSLContext;
    private String[] mCiphers;
    private String[] mProtocols;
    String version;

    public SSLSocketFactoryExtended(InputStream inputStream,String version,boolean safView) throws NoSuchAlgorithmException, KeyManagementException, CertificateException, KeyStoreException, IOException {
        this.version = version;
        this.safView = safView;
        initSSLSocketFactoryEx(null,getTrustManager(inputStream),null);
    }

    public String[] getDefaultCipherSuites() {
        return mCiphers;
    }

    public String[] getSupportedCipherSuites() {
        return mCiphers;
    }

    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        SSLSocketFactory factory = mSSLContext.getSocketFactory();
        SSLSocket ss = (SSLSocket)factory.createSocket(s, host, port, autoClose);

        ss.setEnabledProtocols(mProtocols);
        ss.setEnabledCipherSuites(mCiphers);

        return ss;
    }

    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        SSLSocketFactory factory = mSSLContext.getSocketFactory();
        SSLSocket ss = (SSLSocket)factory.createSocket(address, port, localAddress, localPort);

        ss.setEnabledProtocols(mProtocols);
        ss.setEnabledCipherSuites(mCiphers);

        return ss;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        SSLSocketFactory factory = mSSLContext.getSocketFactory();
        SSLSocket ss = (SSLSocket)factory.createSocket(host, port, localHost, localPort);
        ss.setEnabledProtocols(mProtocols);
        ss.setEnabledCipherSuites(mCiphers);

        return ss;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        SSLSocketFactory factory = mSSLContext.getSocketFactory();
        SSLSocket ss = (SSLSocket)factory.createSocket(host, port);

        ss.setEnabledProtocols(mProtocols);
        ss.setEnabledCipherSuites(mCiphers);

        return ss;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        Logger.v("Soc1");
        SSLSocketFactory factory = mSSLContext.getSocketFactory();
        Logger.v("Soc2");
        Socket socketConn = new Socket();
        Logger.v("Soc3.1");
        socketConn.connect(new InetSocketAddress(host, port), SocketConnectionWorker.IDEAL_TIME);
        Logger.v("Soc3.2");
        SSLSocket ss = (SSLSocket)factory.createSocket(socketConn,host, port,true);
        Logger.v("Soc3.3");
        ss.setEnabledProtocols(mProtocols);
        Logger.v("Soc4");
        ss.setEnabledCipherSuites(mCiphers);
        Logger.v("Soc5");
        return ss;
    }

    private void initSSLSocketFactoryEx(KeyManager[] km, TrustManager[] tm, SecureRandom random) throws NoSuchAlgorithmException, KeyManagementException {

        mSSLContext = SSLContext.getInstance("TLS");
        mSSLContext.init(km, tm, random);

        mProtocols = GetProtocolList();
        mCiphers = GetCipherList();
    }

    protected String[] GetProtocolList() {
        String version = "1.2";
        if((this.version).trim().length() != 0)
            version = this.version;
        String[] protocols = { "TLSv"+version};
        String[] availableProtocols = null;

        SSLSocket socket = null;

        try {
            SSLSocketFactory factory = mSSLContext.getSocketFactory();
            socket = (SSLSocket)factory.createSocket();

            availableProtocols = socket.getSupportedProtocols();
            Logger.v("availableProtocols -s-"+availableProtocols.length);
            for (int i=0;i<availableProtocols.length;i++)
                Logger.v("availableProtocols -"+availableProtocols[i]);
        } catch(Exception e) {
            return new String[]{ "TLSv1" };
        } finally {
            if(socket != null)
                try {
                    socket.close();
                } catch (IOException e) {
                }
        }

        List<String> resultList = new ArrayList<String>();
        for(int i = 0; i < protocols.length; i++) {
            int idx = Arrays.binarySearch(availableProtocols, protocols[i]);
            if(idx >= 0) {
                Logger.v("Added Protocol --"+protocols[i]);
                resultList.add(protocols[i]);
            }else {
                Logger.v("Else protocol --"+protocols[i]);
            }
        }

        return resultList.toArray(new String[0]);
    }

    protected String[] GetCipherList() {
        List<String> resultList = new ArrayList<String>();
        SSLSocketFactory factory = mSSLContext.getSocketFactory();
        for(String s : factory.getSupportedCipherSuites()){
            resultList.add(s);
        }
        return resultList.toArray(new String[resultList.size()]);
    }

    public TrustManager[] getTrustManager(InputStream inputStream) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
        InputStream caInput = new BufferedInputStream(inputStream);
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

// Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        return tmf.getTrustManagers();
    }


}
