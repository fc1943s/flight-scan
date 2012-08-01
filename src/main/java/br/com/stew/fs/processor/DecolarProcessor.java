package br.com.stew.fs.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import br.com.stew.fs.Airport;
import br.com.stew.fs.Processor;

public class DecolarProcessor extends Processor
{

	public DecolarProcessor(Schema schema)
	{
		super(schema);
	}

	@Override
	public String getUrl(Airport from, Airport to, DateTime fromDate, DateTime toDate)
	{
		return "http://www.decolar.com/shop/flights/results/roundtrip/" + from.getDesc() + "/" + to.getDesc() + "/" + fromDate.toString("yyyy-MM-dd") + "/" + toDate.toString("yyyy-MM-dd") + "/1/0/0";
		//return "http://www.decolar.com/shop/flights/results/oneway/" + from.getDesc() + "/" + to.getDesc() + "/" + fromDate.toString("yyyy-MM-dd") + "/1/0/0";
	}

	@Override
	public String getScript(String params)
	{
		InputStream in = getClass().getResourceAsStream("/decolarProcessor.js"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		try
		{
			return params + IOUtils.toString(in);
		}
		catch(IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "";
	}
}
