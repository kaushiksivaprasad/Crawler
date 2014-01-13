

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import websphinx.Crawler;
import websphinx.DownloadParameters;
import websphinx.Page;

public class CrawlerTest extends Crawler {
	private static final long serialVersionUID = 2383514014091378008L;
	private StringBuilder fileContent = new StringBuilder("");
	private int count = 0;
	private File cacheFile = null;
	private String cachedHostName = null;
	public CrawlerTest() {
		DownloadParameters dp = new DownloadParameters();
		dp.changeAcceptedMIMETypes("text/html,text/xml,application/xml");
		dp.changeObeyRobotExclusion(false);
		dp.changeMaxPageSize(500);
		dp.changeUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko Mycrawler contact me @ k_sivapr@live.concordia.ca");
		setMaxDepth(4);
		setDownloadParameters(dp);
		setDomain(Crawler.WEB);
		setLinkType(Crawler.HYPERLINKS);
		File rootFolder = new File(Configuration.directoryToSaveFiles);
		if(!rootFolder.isDirectory())
		{
			try{
				FileUtils.forceMkdir(rootFolder);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	public void visit(Page page) {
		doVisit(page);
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
		}
	}
	public synchronized void doVisit(Page page)
	{
		URL url = page.getURL();
		if(url != null){
			Pattern pttrn = Pattern.compile("^\\S+xml.+$");
			String contentType = page.getContentType();
			Matcher mtcher = pttrn.matcher(contentType);
			String content = page.getContent();
			StringBuilder urlRead = new StringBuilder("");
			urlRead.append(url.getProtocol());
			urlRead.append("://");
			urlRead.append(url.getAuthority());
			urlRead.append(url.getFile());
			String finalUrl = urlRead.toString();
			System.out.println(finalUrl);
			if(mtcher.matches() && WSDLValidator.validateWSDL(content,finalUrl))
			{
//				System.out.println(finalUrl);
				String hostName = url.getHost();
				hostName = hostName.toLowerCase();
				hostName = StringUtils.replace(hostName, "www.", "");
				int index = hostName.indexOf(".");
				hostName = hostName.substring(0,index);
				String path = Configuration.directoryToSaveFiles+hostName+"\\";
				File folder = new File(path);
				try{
					Collection<File> filesFromFolder = null;
					if(!hostName.equalsIgnoreCase(cachedHostName))
					{
						FileUtils.forceMkdir(folder);
						filesFromFolder = FileUtils.listFiles(folder, null, false);
					}
					if(hostName.equalsIgnoreCase(cachedHostName) || filesFromFolder.size() != 0)
					{
						if(!hostName.equalsIgnoreCase(cachedHostName)){
							TreeSet<File> files = new TreeSet<>();
							files.addAll(FileUtils.listFiles(folder, null, false));
							cacheFile = files.last();
							fileContent = new StringBuilder(FileUtils.readFileToString(cacheFile));
							count = StringUtils.countMatches(fileContent,"<IIR>");
						}
						if(count >= Configuration.noOfFilesToBeAggregated)
						{
							String pathOfTheFile = cacheFile.toString();
							StringBuilder builder = new StringBuilder(path);
							builder.append(hostName);
							pathOfTheFile = StringUtils.replace(pathOfTheFile, builder.toString(), "");
							int fileNo = Integer.parseInt(StringUtils.replace(pathOfTheFile, ".txt", ""));
							fileNo++;
							builder.append(fileNo);
							builder.append(".txt");
							cacheFile = new File(builder.toString());
							count = 0;
							fileContent = new StringBuilder();
						}
					}
					else
					{
						StringBuilder builder = new StringBuilder(path);
						builder.append(hostName);
						builder.append("0.txt");
						cacheFile = new File(builder.toString());
						count = 0;
						fileContent = new StringBuilder();
					}
					fileContent.append("<IIR><IIRTitle>");
					fileContent.append(hostName);
					fileContent.append("</IIRTitle>");
					fileContent.append("<IIRUrl>");
					fileContent.append(finalUrl);
					fileContent.append("</IIRUrl>");
					fileContent.append(content);
					fileContent.append("\n</IIR>\n");
					FileUtils.writeStringToFile(cacheFile, fileContent.toString());
					cachedHostName = hostName;
					count++;
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}

			}
		}
	}
}
