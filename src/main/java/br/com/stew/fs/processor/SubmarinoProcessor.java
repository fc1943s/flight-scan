package br.com.stew.fs.processor;

import org.joda.time.DateTime;
import br.com.stew.fs.Airport;
import br.com.stew.fs.Processor;

public class SubmarinoProcessor extends Processor
{

	public SubmarinoProcessor(Schema schema)
	{
		super(schema);
	}

	@Override
	public String getUrl(Airport from, Airport to, DateTime fromDate, DateTime toDate)
	{
		return "http://www.submarinoviagens.com.br/Passagens/selecionarvoo?SomenteIda=false&Origem=--" + from.getDesc() + "---&Destino=--" + to.getDesc() + "---&Origem=--" + to.getDesc() + "---&Destino=--" + from.getDesc() + "---&Data=" + fromDate.toString("dd/MM/yyyy") + "&Data=" + toDate.toString("dd/MM/yyyy") + "&NumADT=1&NumCHD=0&NumINF=0&SomenteDireto=&Hora=&Hora=&selCabin=&Multi=false";
	}

	@Override
	public String getScript(String params)
	{
		return "var best = 0; $('.spanBestPriceNoStop, .spanBestPriceOneStop, .spanBestPriceTwoStop').each(function(k, v) { if($(v).html() == '') return; var num = Number($(v).html().match(/(\\d+),/)[1]); num = Number(num); if(num < best || best == 0) { best = num; } } ); return best;";
	}
}
