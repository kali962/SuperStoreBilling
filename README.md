# 🧾 Superstore Billing System (Java Swing + MySQL)

A desktop-based billing application built using **Java Swing**, **MySQL**, and **iText PDF**. Designed for superstores or retail shops to manage product inventory, generate bills, and export receipts in PDF format.

---

## 🚀 Features

- 🧍 Customer and Product Management  
- 🖥️ User-friendly Java Swing Interface  
- 🧾 Bill generation with PDF export (using iText)  
- 🗃 MySQL database for storing records  
- 🧮 Real-time billing with auto total calculation  

---

## 📂 Folder Structure

SuperstoreBilling/
├── src/ # Java source files
│ └── BillingApp.java
├── lib/ # External libraries (iText jar)
│ └── itextpdf-5.5.13.jar
├── resources/ # Images or other assets
│ └── logo.png
├── database/ # SQL file for setting up the database
│ └── billing.sql
├── README.md # This file
└── .gitignore

---

## 💻 Requirements

- Java JDK (8 or above)
- MySQL Server + Workbench
- Java IDE (NetBeans / VS Code)
- iText PDF library

---

## 🛠 How to Run

### 1. **Set Up Database**
- Open MySQL Workbench
- Run `billing.sql` from `/database` folder to create necessary tables

### 2. **Configure Database Connection**
- In `DBConnect.java`, update your:
  - MySQL **username**
  - **password**
  - database name (default: `superstore`)

```java
String url = "jdbc:mysql://localhost:3306/superstore";
String user = "root";
String pass = "your_password";
3. Add iText Library
Add itextpdf-5.5.13.jar from /lib folder to your project library path

4. Run the App
Compile and run BillingApp.java (or your main frame)

Start adding products and generate bills!

📄 PDF Output Sample
Every bill is exported as a professional PDF with product details and total.

📌 Future Improvements
User login/authentication system

Sales reports with charts

Barcode scanner integration

🤝 Contributing
Pull requests are welcome. For major changes, please open an issue first.

📧 Contact
Made with ❤️ by Students of MUET:  Mazhar Ali & Iqrar Zour.

📩 [mazharalee686@gmail.com]
