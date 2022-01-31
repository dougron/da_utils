package main.java.da_utils.time_signature_utilities.ts_evenness;

import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class TS_Evenness
{

	
	
	public static double getTactusEvennessScore(TimeSignature aTs)
	{
		Double[] tactii = aTs.getTactus();		
		return getDeviationAverage(aTs, tactii);
	}
	
	
	public static double getSuperTactusEvennessScore(TimeSignature aTs)
	{
		Double[] superTactiiAsQuartersPosition = aTs.getSuperTactusAsQuartersPositions();
		if (superTactiiAsQuartersPosition.length == 0) return 0.0;
		Double[] arr = new Double[superTactiiAsQuartersPosition.length];
		for (int i = 0; i < superTactiiAsQuartersPosition.length - 1; i++)
		{
			arr[i] = Math.abs(superTactiiAsQuartersPosition[i] - superTactiiAsQuartersPosition[i + 1]);
		}
		arr[superTactiiAsQuartersPosition.length - 1] = aTs.getLengthInQuarters() - superTactiiAsQuartersPosition[superTactiiAsQuartersPosition.length - 1];
		return getDeviationAverage(aTs, arr);
	}
	
	
	
	public static double getTactusAndSuperTActusEvennessSum(TimeSignature aTs)
	{
		return getTactusEvennessScore(aTs) + getSuperTactusEvennessScore(aTs);
	}

	
	
	private static double getDeviationAverage(TimeSignature aTs, Double[] aGroupingArray)
	{
		double average = aTs.getLengthInQuarters() / aGroupingArray.length;
		double deviationTotal = 0.0;
		for (Double d: aGroupingArray)
		{
			deviationTotal += (Math.abs(average - d));
		}
		return deviationTotal / aGroupingArray.length;
	}

}
