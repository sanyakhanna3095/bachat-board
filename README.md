# 💰 BudgetBoard – Personal Expense and Budget Tracker

A Spring Boot application that helps users track their expenses, manage category-based budgets, receive alerts when spending exceeds limits, and generate insightful monthly and trend-based financial reports in PDF format.

---

## 🚀 Features

- 📊 Track income and expenses by category and date
- 🗂 Set monthly budgets per category
- 🔔 Alert system:
    - Notifies via email when spending exceeds 70% of the budget
    - Prevents expenses if no budget is set for the category
- 🧾 Generate and email PDF reports:
    - Summary report for a selected month
    - Trend report between a date range
- 📅 Monthly budget reset and tracking
- ✅ Validation: Prevents invalid or future date range reports

---

## 🧱 Tech Stack

| Layer     | Technology                 |
|----------|-----------------------------|
| Backend   | Spring Boot, Spring Data JPA |
| Database  | MySQL                      |
| Reports   | iTextPDF                   |
| Email     | Spring Mail                |
| Validation| Java Bean Validation (JSR-303) |

---

## 📦 How to Run

1. Clone the repository
2. Set environment variables (`EMAIL`, `EMAIL_PASSWORD`, `DB_URL`, etc.)
3. Configure application.properties for your database
4. Run the application using Maven or your IDE
5. Access APIs via Postman.

---

