package cz.lala.HipHopStage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class NewsActivity extends Activity implements OnClickListener {
    private static final String URL = "http://www.hiphopstage.cz/feed/rss_2_0.php";
    private static final String FB_PAGE = "https://www.facebook.com/HipHopStage.cz";
    private static final String FB_ID = "fb://page/75012764740";
    private static final String PHONE ="tel:222 264 860";
    
    static final String KEY_ITEM = "item";
    static final String KEY_LINK = "link";
    static final String KEY_TITLE = "title";
    static final String KEY_IMAGE = "image";
    static final String KEY_TEXT = "text";
    static final String KEY_DESC = "description";
   
    Button btnfb;
    Button btnplayer;
    Button btnpho;
    String xml;
    ArrayList<HashMap<String, String>> menuItems;
    ArrayList<Bitmap> imageItems;
    XMLParser parser;
    ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	parser = new XMLParser();
		setContentView(R.layout.act_newsactivity);
		menuItems = new ArrayList<HashMap<String, String>>();
		imageItems = new ArrayList<Bitmap>();
    	btnfb = (Button)findViewById(R.id.act_newsactivity_btnfb);
    	btnfb.setOnClickListener(this);
    	btnplayer = (Button)findViewById(R.id.act_newsactivity_btnplayer);
    	btnplayer.setOnClickListener(this);
        btnpho = (Button)findViewById(R.id.act_newsactivity_btnpho);
        btnpho.setOnClickListener(this);
        listView = (ListView)findViewById(R.id.act_newsactivity_listview);
		super.onCreate(savedInstanceState);
		getXml();
	}
	
	private void getItems(Document doc) {
		NodeList nl = doc.getElementsByTagName(KEY_ITEM);
		
        // looping through all item nodes <item>
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key => value
            map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
            map.put(KEY_LINK, parser.getValue(e, KEY_LINK));
            
            String[] linkImgAndText = getImageLinkAndText(parser.getValue(e, KEY_DESC));
            map.put(KEY_IMAGE, linkImgAndText[0]);
            map.put(KEY_TEXT, linkImgAndText[1]);
            new StahnutiFotky().execute(linkImgAndText[0]);
            // adding HashList to ArrayList
            menuItems.add(map);
        }
		
	}

	private String[] getImageLinkAndText(String value) {
		Pattern imgp = Pattern.compile("((src=\")(.*)(\" align=))");
  		Matcher imgm = imgp.matcher(value);  
  		Pattern textp = Pattern.compile("((\\/> )(.*))");
  		Matcher textm = textp.matcher(value);  
  		String[] linkImgAndText = new String[2];
  		if (imgm.find()) {
			linkImgAndText[0] = imgm.group(3);
			System.out.println(imgm.group(3));
		}
  		else
  		{
  			linkImgAndText[0] = imgm.group(1);
  		}
  		if (textm.find()) {
  			linkImgAndText[1] = textm.group(3);
				System.out.println(textm.group(3));
		}
  		else
  		{
  			linkImgAndText[1] = "text";
  		}
		
		return linkImgAndText;
	}

	private void getXml(){
		new CheckNews().onLaunch();
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.act_newsactivity_btnplayer:
			finish();
			break;
		case R.id.act_newsactivity_btnfb:
			try {
			    this.getPackageManager()
			            .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
			    startActivity( new Intent(Intent.ACTION_VIEW,
			            Uri.parse(FB_ID))); //Trys to make intent with FB's URI
			} catch (Exception e) {
				  startActivity( new Intent(Intent.ACTION_VIEW,
			            Uri.parse(FB_PAGE))); //catches and opens a url to the desired page
			}
			
			break;
		case R.id.act_newsactivity_btnpho:
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse(PHONE));
			startActivity(intent); 
			break;
		default:
			break;
		}
	}
	
	public class CheckNews implements OnCompleteTaskListener
	{

		public void onLaunch()
		{
			try {
			new XMLParser(this).execute(URL);
			} catch (Exception e) {
				System.out.println(e);
			}
			
		}
		@Override
		public void OnCompleteTask(String xml) {
			try {
				Document doc = parser.getDomElement(xml);
				getItems(doc);
			} catch (Exception e) {
				System.out.println(e);
			}
			
		}
		
	}
	private class StahnutiFotky extends AsyncTask<String, Void, Void>
	{
		Bitmap bmp = null;

		@Override
		protected Void doInBackground(String... params) {
			URL url;
			try {
				url = new URL(params[0]);
				bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			imageItems.add(bmp);		
			checkComplete();
			super.onPostExecute(result);
		}
	}
	public void checkComplete() {
		if(imageItems.size()==menuItems.size())
		{
			NewsActivity_Adapter adapter = new NewsActivity_Adapter(this, imageItems, menuItems);
			listView.setAdapter(adapter);
		/*	for (int i = 0; i < menuItems.size(); i++) {
				System.out.println(menuItems.get(i).get(KEY_TITLE));
				System.out.println(menuItems.get(i).get(KEY_TEXT));
			}*/
			
		}
		
	}

}
