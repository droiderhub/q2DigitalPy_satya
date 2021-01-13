package com.tarang.dpq2.base.terminal_sdk.utils;


public class LuhnAlgorithm {

    // prefix number of digits
    private static final Integer DIGITS1 = 1;

    private static final Integer DIGITS2 = 2;

    private static final Integer DIGITS3 = 3;

    private static final Integer DIGITS4 = 4;

    // card types
    private static final Integer INVALID = -1;

    private static final Integer VISA = 1;

    private static final Integer MASTER = 2;

    private static final Integer MAESTRO = 3;

    private static final Integer AMERICAN_EXPRESS = 4;

    private static final Integer EN_ROUTE = 5;

    private static final Integer DINERS_CLUB = 6;

    // prefix values
    private static final Integer VISA_PREFIX_ONE = 4;

    private static final Integer MASTER_PREFIX_ONE = 51;

    private static final Integer MASTER_PREFIX_TWO = 55;

    private static final Integer AMERICAN_EXPRESS_PREFIX_ONE = 34;

    private static final Integer AMERICAN_EXPRESS_PREFIX_TWO = 37;

    private static final Integer EN_ROUTE_PREFIX_ONE = 2014;

    private static final Integer EN_ROUTE_PREFIX_TWO = 2149;

    private static final Integer DINERS_CLUB_PREFIX_ONE = 36;

    private static final Integer DINERS_CLUB_PREFIX_TWO = 38;

    private static final Integer DINERS_CLUB_PREFIX_THREE = 300;

    private static final Integer DINERS_CLUB_PREFIX_FOUR = 305;


    // card digits length
    private static final Integer VISA_MIN_LENGTH = 13;

    private static final Integer VISA_MAX_LENGTH = 16;

    private static final Integer MASTER_LENGTH = 16;

    private static final Integer AMERICAN_EXPRESS_LENGTH = 15;

    private static final Integer MAESTRO_MIN_LENGTH = 12;

    private static final Integer MAESTRO_MAX_LENGTH = 19;

    private static final Integer EN_ROUTE_LENGTH = 15;

    private static final Integer DINERS_CLUB_ROUTE_LENGTH = 14;

    /**
     * Validate a Card number
     */
    public boolean validate(String number, Long type) {

        //int vType = getCardID(number, type);
       // if (vType == type || type == MAESTRO.intValue()) {
            return luhnCheck(number);
      //  }
      //  return false;
    }


    /**
     * Get the Card type returns the credit card type INVALID = -1; VISA = 0;
     * MASTERCARD = 1; AMERICAN_EXPRESS = 2; EN_ROUTE = 3; DINERS_CLUB = 4;
     */
    private int getCardID(String number, Long type) {
        int cardType = INVALID;

        Integer digit1 = Integer.valueOf(number.substring(0, DIGITS1));
        Integer digit2 = Integer.valueOf(number.substring(0, DIGITS2));
        Integer digit3 = Integer.valueOf(number.substring(0, DIGITS3));
        Integer digit4 = Integer.valueOf(number.substring(0, DIGITS4));

		/*
         * ----* VISA prefix=4* ---- length=13 or 16 (can be 15 too!?!
		 * maybe)
		 */
        if (digit1.equals(VISA_PREFIX_ONE)) {
            if (number.length() == VISA_MIN_LENGTH || number.length() == VISA_MAX_LENGTH) {
                cardType = VISA;
            }
        } else if (digit2 >= MASTER_PREFIX_ONE && digit2 <= MASTER_PREFIX_TWO) {
            /*
			 * ----------* MASTERCARD prefix= 51 ... 55* ---------- length= 16
			 */
            if (number.length() == MASTER_LENGTH) {
                cardType = MASTER;
            }
        } else if (digit2.equals(AMERICAN_EXPRESS_PREFIX_ONE) || digit2.equals(AMERICAN_EXPRESS_PREFIX_TWO)) {
			/*
			 * ----* AMEX prefix=34 or 37* ---- length=15
			 */
            if (number.length() == AMERICAN_EXPRESS_LENGTH) {
                cardType = AMERICAN_EXPRESS;
            }
        } else if (digit4.equals(EN_ROUTE_PREFIX_ONE) || digit4.equals(EN_ROUTE_PREFIX_TWO)) {
			/*
			 * -----* ENROU prefix=2014 or 2149* ----- length=15
			 */
            if (number.length() == EN_ROUTE_LENGTH) {
                cardType = EN_ROUTE;
            }
        } else if ((digit2.equals(DINERS_CLUB_PREFIX_ONE)
                || digit2.equals(DINERS_CLUB_PREFIX_TWO)
                || (digit3 >= DINERS_CLUB_PREFIX_THREE && digit3 <= DINERS_CLUB_PREFIX_FOUR))
                && (number.length() == DINERS_CLUB_ROUTE_LENGTH)) {
			
			/*
			 * -----* DCLUB prefix=300 ... 305 or 36 => and <= 38* ----- length=14
			 */
            cardType = DINERS_CLUB;
        } else if (type == MAESTRO.intValue()) {
            // has to updated with maestro card prifix
            if (number.length() >= MAESTRO_MIN_LENGTH && number.length() <= MAESTRO_MAX_LENGTH) {
                cardType = MAESTRO;
            }
        } else {
            cardType = INVALID;
        }
        return cardType;
    }

    private boolean luhnCheck(String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        final int mod = 10;
        final int nine = 9;
        try {
            for (int i = ccNumber.length() - 1; i >= 0; i--) {
                int n;
                n = Integer.parseInt(ccNumber.substring(i, i + 1));
                if (alternate) {
                    n *= 2;
                    if (n > nine) {
                        n = (n % mod) + 1;
                    }
                }
                sum += n;
                alternate = !alternate;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return (sum % mod == 0);
    }

}
