package me.arganzheng.project.reading.model;

import java.util.List;

import me.arganzheng.project.reading.util.StringUtils;

public class Book {

	private static final int MAX_SUMMARY_LENGTH = 1000;

	private String isbn10;
	private String isbn13;

	private String title;
	private String subtitle;

	private String image;

	private String authors;
	private String pubdate;
	private String summary;
	private String pageCount;

	private String tags;

	private String detailLink;

	private String owners;

	// One books may be owned by many user, refer to book_owner_ship table
	private List<BookOwnership> ownerships;

	public String getIsbn10() {
		return isbn10;
	}

	public String getIsbn() {
		// 优先返回isbn13
		if (StringUtils.isNotEmpty(isbn13)) {
			return isbn13;
		} else {
			return isbn10;
		}
	}

	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}

	public String getIsbn13() {
		return isbn13;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		// summary截断保存1k
		if (summary.length() > MAX_SUMMARY_LENGTH) {
			this.summary = summary.substring(0, MAX_SUMMARY_LENGTH - 3) + "...";
		} else {
			this.summary = summary;
		}
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public String getDetailLink() {
		return detailLink;
	}

	public void setDetailLink(String detailLink) {
		this.detailLink = detailLink;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getOwners() {
		return owners;
	}

	public void setOwners(String owners) {
		this.owners = owners;
	}

	public List<BookOwnership> getOwnerships() {
		return ownerships;
	}

	public void setOwnerships(List<BookOwnership> ownerships) {
		this.ownerships = ownerships;
	}

	@Override
	public String toString() {
		return "Book [isbn10=" + isbn10 + ", isbn13=" + isbn13 + ", title="
				+ title + ", subtitle=" + subtitle + ", image=" + image
				+ ", authors=" + authors + ", pubdate=" + pubdate
				+ ", summary=" + summary + ", pageCount=" + pageCount
				+ ", tags=" + tags + ", detailLink=" + detailLink + ", owners="
				+ owners + ", ownerships=" + ownerships + "]";
	}

}
