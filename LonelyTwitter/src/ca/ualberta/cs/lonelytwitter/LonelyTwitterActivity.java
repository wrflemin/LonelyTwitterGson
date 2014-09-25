package ca.ualberta.cs.lonelytwitter;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import ca.ualberta.cs.lonelytwitter.data.GsonDataManager;
import ca.ualberta.cs.lonelytwitter.data.IDataManager;

public class LonelyTwitterActivity extends Activity {

	private IDataManager dataManager;

	private EditText bodyText;

	private ListView oldTweetsList;

	private ArrayList<Tweet> tweets;

	private ArrayAdapter<Tweet> tweetsViewAdapter;
	
	private Summary mySummary;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		dataManager = new GsonDataManager(this);
		mySummary = new Summary();
		
		bodyText = (EditText) findViewById(R.id.body);
		oldTweetsList = (ListView) findViewById(R.id.oldTweetsList);
		
		
	}

	@Override
	protected void onStart() {
		super.onStart();

		tweets = dataManager.loadTweets();
		tweetsViewAdapter = new ArrayAdapter<Tweet>(this,
				R.layout.list_item, tweets);
		oldTweetsList.setAdapter(tweetsViewAdapter);
		
	}

	public void save(View v) {

		String text = bodyText.getText().toString();

		Tweet tweet = new Tweet(new Date(), text);
		tweets.add(tweet);

		tweetsViewAdapter.notifyDataSetChanged();

		bodyText.setText("");
		dataManager.saveTweets(tweets);
	}

	public void clear(View v) {

		tweets.clear();
		tweetsViewAdapter.notifyDataSetChanged();
		dataManager.saveTweets(tweets);
	}
	
	public void summary(View v){
		createSummary();
		
		// intent to new activity
		Intent intent = new Intent(this, SummaryActivity.class);
		//initialize
		Bundle extras = intent.getExtras();
		
		String val1 = Long.toString( mySummary.getAvgLength());
		extras.putString("avg", val1);
		
		//String val2 = Long.toString( mySummary.getNumber());
		//extras.putString("num", val2);		
		
	}
	
	private void createSummary(){
		mySummary.setAvgLength(getAverageLength());
		mySummary.setNumber(getNumber());
	}
	
	private long getNumber(){
		return tweets.size();
	}
	
	private long getAverageLength(){
		long avg = 0;
		String body;
		Tweet myTweet;
		
		for (int i=0; i < tweets.size() ; i++){
			myTweet= tweets.get(i);
			body = myTweet.getTweetBody();
			avg += body.length();
		}
		return (avg/tweets.size());
	}
}