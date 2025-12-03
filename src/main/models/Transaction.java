package main.models;

public class Transaction {
  private String id;
  private String memberId;
  private String bookId;
  private String borrowDate;
  private String dueDate;
  private String returnDate;
  private String status;

  public Transaction() {
  }

  public Transaction(String id, String memberId, String bookId, String borrowDate, String dueDate, String returnDate, String status) {
    this.id = id;
    this.memberId = memberId;
    this.bookId = bookId;
    this.borrowDate = borrowDate;
    this.dueDate = dueDate;
    this.returnDate = returnDate;
    this.status = status;
  }

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  public String getMemberId() {
    return memberId;
  }
  public void setMemberId(String memberId) {
    this.memberId = memberId;
  }

  public String getBookId() {
    return bookId;
  }
  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public String getBorrowDate() {
    return borrowDate;
  }
  public void setBorrowDate(String borrowDate) {
    this.borrowDate = borrowDate;
  }

  public String getDueDate() {
    return dueDate;
  }
  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }

  public String getReturnDate() {
    return returnDate;
  }
  public void setReturnDate(String returnDate) {
    this.returnDate = returnDate;
  }

  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
}