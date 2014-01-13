import java.io.IOException;
import java.net.URL;

import websphinx.Link;

public class Test {
	public static void main(String[] args) throws IOException {
//		String x = "kaushik.ca.com";
//		System.out.println(x.substring(0,x.indexOf("."))+"xxx");
//		Pattern pttrn = Pattern.compile("[a-d1-7]");
//		Matcher matcher = pttrn.matcher("82a9bzdd");
//		System.out.println(StringUtils.countMatches("<IIR>kaushik <IIR> hfslkfjklsdjkl sdf </IIR> sdfds <IIR>","<IIR>"));
		CrawlerTest crawler = new CrawlerTest();
		URL url = new URL("http://remotemethods.com/home/business");
		Link link = new Link(url);
		crawler.setRoot(link);
		crawler.run();
//		File rootFolder = new File(Configuration.directoryToSaveFiles+"//temp//temp1//");
//		System.out.println(FileUtils.listFiles(rootFolder, null, false).size());
//		TreeSet<File> set = new TreeSet<>();
//		set.addAll(FileUtils.listFiles(rootFolder, null, false));
//		System.out.println(set);
	}
}
