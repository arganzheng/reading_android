package me.arganzheng.project.reading;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import me.arganzheng.project.reading.model.Book;
import me.arganzheng.project.reading.util.BookUtils;
import me.arganzheng.project.reading.util.StringUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = MainActivity.class.getSimpleName();

	// scan, preview, link buttons
	private Button scanBtn, previewBtn, linkBtn;
	// author, title, description, date and rating count text views
	private TextView authorText, titleText, summaryText, pubDateText,
			pageCountText, tagText;
	// layout for star rating
	private LinearLayout starLayout;
	// thumbnail
	private ImageView thumbView;
	// star views
	private ImageView[] starViews;
	// thumbnail bitmap
	private Bitmap thumbImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		scanBtn = (Button) findViewById(R.id.scan_btn);
		scanBtn.setOnClickListener(this);

		// preview and link buttons
		previewBtn = (Button) findViewById(R.id.preview_btn);
		previewBtn.setVisibility(View.GONE);
		previewBtn.setOnClickListener(this);
		linkBtn = (Button) findViewById(R.id.link_btn);
		linkBtn.setVisibility(View.GONE);
		linkBtn.setOnClickListener(this);

		// ui items
		authorText = (TextView) findViewById(R.id.book_author);
		titleText = (TextView) findViewById(R.id.book_title);
		summaryText = (TextView) findViewById(R.id.book_summary);
		pubDateText = (TextView) findViewById(R.id.book_pubDate);
		starLayout = (LinearLayout) findViewById(R.id.star_layout);
		pageCountText = (TextView) findViewById(R.id.book_page_count);
		tagText = (TextView) findViewById(R.id.book_tag);
		thumbView = (ImageView) findViewById(R.id.thumb);

		// star views
		starViews = new ImageView[5];
		for (int s = 0; s < starViews.length; s++) {
			starViews[s] = new ImageView(this);
		}
	}

	public void onClick(View v) {
		// check for scan button
		if (v.getId() == R.id.scan_btn) {
			// instantiate ZXing integration class
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			// start scanning
			scanIntegrator.initiateScan();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// retrieve result of scanning - instantiate ZXing object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		// check we have a valid result
		if (scanningResult != null) {
			// get content from Intent Result
			String scanContent = scanningResult.getContents();
			// get format name of data scanned
			String scanFormat = scanningResult.getFormatName();
			Log.v(TAG, "content: " + scanContent + " - format: " + scanFormat);
			if (scanContent != null && scanFormat != null
					&& scanFormat.equalsIgnoreCase("EAN_13")) {
				// fetch search results
				new GetBookInfo().execute(scanContent);
			} else {
				// not ean
				Toast toast = Toast.makeText(getApplicationContext(),
						"Not a valid scan!", Toast.LENGTH_SHORT);
				toast.show();
			}
		} else {
			// invalid scan data or scan canceled
			Toast toast = Toast.makeText(getApplicationContext(),
					"No book scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	// class to fetch book info
	private class GetBookInfo extends AsyncTask<String, Void, Book> {
		@Override
		protected Book doInBackground(String... isbns) {
			Book book = null;
			try {
				book = BookUtils.getBookByISBN(isbns[0]);
			} catch (IOException e) {
				Log.e(TAG, "getBookByISBN failed!", e);
			}
			return book;
		}

		protected void onPostExecute(Book book) {
			titleText.setText(book.getTitle());
			authorText.setText(book.getAuthors());
			pubDateText.setText(book.getPubdate());
			summaryText.setText(book.getSummary());
			pageCountText.setText(book.getPageCount());
			tagText.setText(book.getTags());

			// image thumbnail
			String imgUrl = book.getImage();
			new GetBookThumb().execute(imgUrl);
		}
	}

	// class to fetch thumbnail
	private class GetBookThumb extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... thumbURLs) {
			try {
				// attempt to download thumbnail image using passed URL
				URL thumbURL = new URL(thumbURLs[0]);
				URLConnection thumbConn = thumbURL.openConnection();
				thumbConn.connect();
				InputStream thumbIn = thumbConn.getInputStream();
				BufferedInputStream thumbBuff = new BufferedInputStream(thumbIn);
				thumbImg = BitmapFactory.decodeStream(thumbBuff);

				thumbBuff.close();
				thumbIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return StringUtils.EMPTY;
		}

		protected void onPostExecute(String result) {
			// show the image
			thumbView.setImageBitmap(thumbImg);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is
		// present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
