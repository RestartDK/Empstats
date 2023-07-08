package model;

public class ProcessData {
    //Assume the data for the percentage of email's read daily is in order by date in an int[]
    static public int averageRate(int[] rate) {
        int average = 0;
        for (int i = 0; i < rate.length; i++) {
            average += rate[i];
        }
        average = average/rate.length;

        return average;
    }

    public static String productivityCalc(int avread, int avcall) {
    	float productivityRead = 0;
    	float productivityCall = 0;
    	float productivityAll = 0;
    	String productivityFinal = null;
    	
    	// Determining productivity for email read rates
    	if (avread <= 30) 
    	{
    		productivityRead = 1;		//low
    	}
    	else if (avread <= 50) 			
    	{
    		productivityRead = 2;		//medium
    	}
    	else 
    	{
    		productivityRead = 3;		//high
    	}
    	
    	
    	// Determining producitivity for call rates
    	if (avcall <= 30) 
    	{
    		productivityCall = 1;		//low
    	}
    	else if (avcall <= 50) 
    	{
    		productivityCall = 2;		//medium
    	}
    	else 
    	{
    		productivityCall = 3;		//high
    	}
    	
    	// Calculating overall productivity for the user
    	productivityAll = productivityRead + productivityCall;
    	
    	if (productivityAll >= 2.1) 
    	{
    		productivityFinal = "high";
    	}
    	else if (productivityAll >= 1.1) 
    	{
    		productivityFinal = "medium";
    	}
    	else 
    	{
    		productivityFinal = "low";
    	}
    	
    	return productivityFinal;
    }
    
    public static boolean naiveSearch(String txt, String pat){
        int M = pat.length();
        int N = txt.length();
        boolean pattern = false;

        for (int i = 0; i <= N - M; i++) {
            int j;

            // For current index i, check for pattern match
            for (j = 0; j < M; j++)
                if (txt.toLowerCase().charAt(i + j) != pat.toLowerCase().charAt(j))
                {
                    break;
                }

            if (j == M) // if pat[0...M-1] = txt[i, i+1, ...i+M-1]
            {
                pattern = true;
            }
        }
        
        // if a pattern has been found, return boolean statement
        if (pattern == false) {
        	return pattern;
        }
        return pattern;
    }

     
}
