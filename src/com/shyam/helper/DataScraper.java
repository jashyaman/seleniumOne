package com.shyam.helper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.shyam.model.Person;

public class DataScraper {

	static Logger logger = Logger.getLogger(DataScraper.class.getName());
	
	public static Set<Person> fetchDataFromUrl(String srcUrl) throws InterruptedException, ExecutionException, IOException  {
		Set<Person> setOfPersons = new TreeSet<Person>();
		
		
		//URL url = new URL(srcUrl);
		
		// tried below code just for kicks.
		
		/*ExecutorService executor = Executors.newFixedThreadPool(1);
		
		Future<Response> response = executor.submit(new Request(url));
		
		InputStream body = response.get().getBody();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while((length = body.read(buffer)) != -1)
		{
			bos.write(buffer, 0	,length);
		}
		
		String result = bos.toString("UTF-8");

		
		
		executor.shutdown();
		*/
		
		
		Document doc = Jsoup.connect(srcUrl).get();
		Elements tbody = doc.getElementsByTag("tbody");
		
		Element tableBody = tbody.get(0);
		
		Elements trList = tableBody.getElementsByTag("tr");
		
		int ctr = 1;
		for(Element tr : trList)
		{
			String name = tr.getElementsByTag("td").get(0).html();
			String position = tr.getElementsByTag("td").get(1).html();
			String office = tr.getElementsByTag("td").get(2).html();
			int age = Integer.parseInt(tr.getElementsByTag("td").get(3).html());
			String date = tr.getElementsByTag("td").get(4).html();
			double salary = Double.parseDouble(tr.getElementsByTag("td").get(5).html().substring(1).replace(",", ""));
			
			Person person = new Person(ctr++,name,position,office,age,date,salary);
			logger.info("in element loop : new person :- " + person.toString());
			setOfPersons.add(person);
		}
		
		return setOfPersons;
	}

}
