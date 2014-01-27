package me.arganzheng.project.reading.util;

import java.io.IOException;
import java.text.ParseException;

import me.arganzheng.project.reading.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class BookUtils {

	private static final String API_URL = "https://api.douban.com/v2/book/isbn/";
	private static final String TAG = BookUtils.class.getSimpleName();

	/**
	 * 调用Douban API获取图书信息
	 * 
	 * @param isbn
	 * @return
	 */
	public static Book getBookByISBN(String isbn) throws IOException {
		String url = API_URL + isbn;

		String bookJson = WebUtils.doGet(url, null);

		Book book = jsonToBook(bookJson);

		return book;
	}

	private static Book jsonToBook(String bookJson) throws IOException {
		try {
			JSONObject bookObject = new JSONObject(bookJson);

			Book book = new Book();
			book.setIsbn10(bookObject.getString("isbn10"));
			book.setIsbn13(bookObject.getString("isbn13"));
			book.setTitle(bookObject.getString("title"));
			book.setSubtitle(StringUtils.trimToNull(bookObject
					.getString("subtitle")));
			book.setImage(bookObject.getString("image"));
			book.setPageCount(bookObject.getString("pages"));
			book.setPubdate(bookObject.getString("pubdate"));
			book.setSummary(bookObject.getString("summary"));

			StringBuilder authors = new StringBuilder();
			try {
				JSONArray authorArray = bookObject.getJSONArray("author");
				for (int i = 0; i < authorArray.length(); i++) {
					if (i > 0) {
						authors.append(", ");
					}
					authors.append(authorArray.getString(i));
				}
			} catch (JSONException jse) {
			}
			book.setAuthors(authors.toString());

			StringBuilder tags = new StringBuilder();
			try {
				JSONArray tagArray = bookObject.getJSONArray("tags");
				for (int i = 0; i < tagArray.length(); i++) {
					if (i > 0) {
						tags.append(", ");
					}
					JSONObject tagObject = tagArray.getJSONObject(i);
					tags.append(tagObject.getString("title"));
				}
			} catch (JSONException jse) {
			}
			book.setTags(tags.toString());

			book.setDetailLink(bookObject.getString("alt"));
			return book;
		} catch (Exception e) {
			Log.e(TAG, "parse json to object failed!", e);
			throw new IOException(e);
		}
	}

	public static void main(String[] args) throws IOException, ParseException {
		System.out.println("Main");
		String url = API_URL + "7505715666";
		System.out.println("Main");
		String bookJson = WebUtils.doGet(url, null);
		System.out.println(bookJson);

		Book book = jsonToBook(bookJson);
		System.out.println(book);
	}
}
