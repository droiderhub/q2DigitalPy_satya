package com.tarang.dpq2.base.jpos_class;

import org.jpos.iso.IFA_BINARY;
import org.jpos.iso.IFA_BITMAP;
import org.jpos.iso.IFA_LLBINARY;
import org.jpos.iso.IFA_LLCHAR;
import org.jpos.iso.IFA_LLLBINARY;
import org.jpos.iso.IFA_LLLCHAR;
import org.jpos.iso.IFA_LLLLLLBINARY;
import org.jpos.iso.IFA_LLLNUM;
import org.jpos.iso.IFA_LLNUM;
import org.jpos.iso.IFA_NUMERIC;
import org.jpos.iso.IFB_BINARY;
import org.jpos.iso.IF_CHAR;
import org.jpos.iso.ISOBasePackager;
import org.jpos.iso.ISOFieldPackager;

public class MadaPackager extends ISOBasePackager {

    protected ISOFieldPackager fld[] = {
            /* 000 */new IFA_NUMERIC(4, "Message Type Indicator"), // Done
            /* 001 */new IFA_BITMAP(16, "Secondary Bitmap"), // Bitmap Need more clarification
            /* 002 */new IFA_LLNUM(19, "Primary Account number"), //Done
            /* 003 */new IFA_NUMERIC(6, "Processing Code"), //Done
            /* 004 */new IFA_NUMERIC(12, "Amount, Transaction"), //Done
            /* 005 */new IFA_NUMERIC(12, "Amount, Settlement"),
            /* 006 */new IFA_NUMERIC(12, "Amount, Cardholder billing"),
            /* 007 */new IFA_NUMERIC(10, "Date and time, transmission"), //Done
            /* 008 */new IFA_NUMERIC(12, "Amount, Cardholder billing fee"),
            /* 009 */new IFA_NUMERIC(8, "Conversion rate, Settlement"),
            /* 010 */new IFA_NUMERIC(8, "Conversion rate, Cardholder billing"),

            /* 011 */new IFA_NUMERIC(6, "Systems trace audit number"), // Need clarification
            /* 012 */new IFA_NUMERIC(12, "Time, Local Transaction"), //Done
            /* 013 */new IFA_NUMERIC(4, "Date, Local Transaction"),
            /* 014 */new IFA_NUMERIC(4, "Date, Expiration"), // Not ther
            /* 015 */new IFA_NUMERIC(4, "Date, Settlement"),
            /* 016 */new IFA_NUMERIC(4, "Date, Conversion"),
            /* 017 */new IFA_NUMERIC(4, "Date, Capture"),
            /* 018 */new IFA_NUMERIC(4, "Merchant type"),
            /* 019 */new IFA_NUMERIC(3, "Country code, Acquiring institution"),
            /* 020 */new IFA_NUMERIC(3, "Country code, Primary account number"),

            /* 021 */new IFA_NUMERIC(3, "Country code, Forwarding institution"),
            /* 022 */new IFA_NUMERIC(12, "Point of service data code"), //Done
            /* 023 */new IFA_NUMERIC(3, "Card sequence number"), // Need clarification
            /* 024 */new IFA_NUMERIC(3, "Function code"), //Done
            /* 025 */new IFA_NUMERIC(4, "Message Reason Code"), // Done
            /* 026 */new IFA_NUMERIC(4, "Card Acceptor Business Code"), // Yet to set from TMS
            /* 027 */new IFA_NUMERIC(1, "Authorization ID Response Length"),
            /* 028 */new IFA_NUMERIC(6, "Reconciliation Date"), // Yet to set
            /* 029 */new IFA_NUMERIC(1 + 8, "Amount, Settlement Fee"),
            /* 030 */new IFA_NUMERIC(12, "Original Amount"), // Yet to set -

            /* 031 */new IFA_NUMERIC(1 + 8, "Amount, Settle Processing Fee"),
            /* 032 */new IFA_LLNUM(11, "Acquirer institution identification code"), // Get it from Terminal Registration
            /* 033 */new IFA_LLNUM(11, "Forwarding institution identification code"),
            /* 034 */new IFA_LLCHAR(19, "Primary account number, extended"),
            /* 035 */new IFA_LLBINARY(37, "Track 2 data"), // Done
            /* 036 */new IFA_LLLCHAR(104, "Track 3 data"),
            /* 037 */new IF_CHAR(12, "Retrieval reference number"), // Yet to set need clarification
            /* 038 */new IF_CHAR(6, "Approval Code"), // Yet to set
            /* 039 */new IF_CHAR(3, " Action Code"), // Yet to set
            /* 040 */new IFA_NUMERIC(3, "Service Restriction Code"),

            /* 041 */new IF_CHAR(16, "Card Acceptor Terminal ID"), // Get it from Terminal Registration
            /* 042 */new IF_CHAR(15, "Card Acceptor ID Code"), // Get it from Terminal Registration
            /* 043 */new IF_CHAR(40, "Card Acceptor Name Location"),
            /* 044 */new IFA_LLCHAR(99, "Additional Response Data"), // Not applicable
            /* 045 */new IFA_LLCHAR(76, "Track 1 data"),
            /* 046 */new IFA_LLNUM(12, "Amounts, Fees"),
            /* 047 */new IFA_LLLCHAR(999, " Card Scheme Sponsor ID & Additional data"), // Yet to set
            /* 048 */new IFA_LLLBINARY(999, "Additional data - private"), // Yet to set
            /* 049 */new IFA_NUMERIC(3, "Currency code, Transaction"), // Done
            /* 050 */new IFA_NUMERIC(3, "Currency code, Reconciliation"), // Yet to set reconsilation

            /* 051 */new IFA_NUMERIC(3, "Currency code, Cardholder billing"),
            /* 052 */new IFB_BINARY(8, "Personal identification number [PIN] data"), // Yet to set  Encripted pin
            /* 053 */new IFA_LLBINARY(48, "Security related control information"), // Yet to set
            /* 054 */new IFA_LLLCHAR(120, "Amounts, additional"), // Yet to set 2
            /* 055 */new IFA_LLLBINARY(255, "ICC related data"), // Need to Clarification
            /* 056 */new IFA_LLNUM(58, " Original Data Elements "), // Yet to set 2
            /* 057 */new IFA_LLLNUM(3, "Authorization Life-cycle Code"),
            /* 058 */new IFA_LLLNUM(11, "Authorizing agent institution Id Code"),
            /* 059 */new IFA_LLLCHAR(999, " Transport Data "), // Not applicable
            /* 060 */new IFA_LLLCHAR(999, "Reserved for national use"),

            /* 061 */new IFA_LLLCHAR(999, "Reserved for national use"),
            /* 062 */new IFA_LLLCHAR(999, "Terminal Status- Private"), // Yet to set
            /* 063 */new IFA_LLLCHAR(999, "Reserved for private use"),
            /* 064 */new IFB_BINARY(8, "Message authentication code"), // need clarification
            /* 065 */new IFA_BINARY(8, "Reserved for ISO use"),
            /* 066 */new IFA_NUMERIC(1, "Settlement Code"),
            /* 067 */new IFA_NUMERIC(2, "Extended Payment Code"),
            /* 068 */new IFA_NUMERIC(3, "Country code, receiving institution"),
            /* 069 */new IFA_NUMERIC(3, "Country code, settlement institution"),
            /* 070 */new IFA_NUMERIC(3, "Network Management Information Code"),

            /* 071 */new IFA_NUMERIC(8, "Message number"),
            /* 072 */new IFA_LLLCHAR(999, "Data record"), // neet to set 2
            /* 073 */new IFA_NUMERIC(6, "Date, action"),
            /* 074 */new IFA_NUMERIC(10, "Credits, number"),
            /* 075 */new IFA_NUMERIC(10, "Credits, checkReversal number"),
            /* 076 */new IFA_NUMERIC(10, "Debits, number"),
            /* 077 */new IFA_NUMERIC(10, "Debits, checkReversal number"),
            /* 078 */new IFA_NUMERIC(10, "Transfer, number"),
            /* 079 */new IFA_NUMERIC(10, "Transfer, checkReversal number"),
            /* 080 */new IFA_NUMERIC(10, "Inquiries, number"),

            /* 081 */new IFA_NUMERIC(10, "Authorizations, number"),
            /* 082 */new IFA_NUMERIC(12, "Credits, Processing Fee Amount"),
            /* 083 */new IFA_NUMERIC(12, "Credits, Transaction Fee Amount"),
            /* 084 */new IFA_NUMERIC(12, "Debits, Processing Fee Amount"),
            /* 085 */new IFA_NUMERIC(12, "Debits, Transaction Fee Amount"),
            /* 086 */new IFA_NUMERIC(16, "Credits, amount"),
            /* 087 */new IFA_NUMERIC(16, "Credits, checkReversal amount"),
            /* 088 */new IFA_NUMERIC(16, "Debits, amount"),
            /* 089 */new IFA_NUMERIC(16, "Debits, checkReversal amount"),
            /* 090 */new IFA_NUMERIC(42, "Original Data Elements"),

            /* 091 */new IF_CHAR(1, "File Update Code"),
            /* 092 */new IFA_NUMERIC(3, "Country code, transaction Orig. Inst."),
            /* 093 */new IFA_LLNUM(11, "Transaction Dest. Inst. Id code"),
            /* 094 */new IFA_LLNUM(11, "Transaction Orig. Inst. Id code"),
            /* 095 */new IF_CHAR(42, "Replacement Amounts"),
            /* 096 */new IFA_LLLBINARY(999, "Key management data"),
            /* 097 */new IFA_NUMERIC(1 + 16, "Amount, Net Settlement"),
            /* 098 */new IF_CHAR(25, "Payee"),
            /* 099 */new IFA_LLCHAR(11, "Settlement institution Id code"),
            /* 100 */new IFA_LLNUM(11, "Receiving institution Id code"),

            /* 101 */new IFA_LLCHAR(17, "File name"),
            /* 102 */new IFA_LLCHAR(28, "Account identification 1"),
            /* 103 */new IFA_LLCHAR(28, "Account identification 2"),
            /* 104 */new IFA_LLLCHAR(999, "Reserved for ISO use"),
            /* 105 */new IFA_NUMERIC(12, "Credits, Chargeback amount"),
            /* 106 */new IFA_NUMERIC(12, "Debits, Chargeback amount"),
            /* 107 */new IFA_NUMERIC(12, "Credits, Chargeback number"),
            /* 108 */new IFA_NUMERIC(12, "Debits, Chargeback number"),
            /* 109 */new IFA_NUMERIC(12, "Credits, Fee amounts"),
            /* 110 */new IFA_NUMERIC(12, "Debits, Fee amounts"),

            /* 111 */new IFA_LLLCHAR(999, "Reserved for ISO use"),
            /* 112 */new IFA_LLLCHAR(999, "Reserved for ISO use"),
            /* 113 */new IFA_LLLCHAR(999, "Reserved for ISO use"),
            /* 114 */new IFA_LLLCHAR(999, "Reserved for ISO use"),
            /* 115 */new IFA_LLLCHAR(999, "Reserved for ISO use"),
            /* 116 */new IFA_LLLCHAR(999, "Reserved for national use"),
            /* 117 */new IFA_LLLCHAR(999, "Reserved for national use"),
            /* 118 */new IFA_LLLNUM(10, "Payments, Number"),
            /* 119 */new IFA_LLLNUM(10, "Payments, Reversal Number"),
            /* 120 */new IFA_LLLCHAR(999, "Reserved for national use"),

            /* 121 */new IFA_LLLCHAR(999, "Reserved for national use"),
            /* 122 */new IFA_LLLCHAR(999, "Reserved for national use"),
            /* 123 */new IFA_LLLCHAR(15, "POS Data Code"),
            /* 124 */new IFA_LLLCHAR(999, " mada POS Terminal Reconciliation Totals"), // neet to set - reconsilation
            /* 125 */new IFA_LLLCHAR(40, "Network management information"),
            /* 126 */new IFA_LLLCHAR(999, "Reserved for private use"),
            /* 127 */new IFA_LLLLLLBINARY(999, "Field 127"),
            /* 128 */new IFB_BINARY(8, "Message authentication code field")}; // neet to set - reconsilation

    /**
     * Default constructor.
     */
    public MadaPackager() {
        super();
        setFieldPackager(this.fld);
    }
}
