from flask import Flask, request, jsonify
from flask_cors import CORS
from flask_jwt_extended import JWTManager, create_access_token, jwt_required, get_jwt_identity
import sqlite3
import os
import csv

app = Flask(__name__)
CORS(app)
app.config['JWT_SECRET_KEY'] = 'super-secret-key'
jwt = JWTManager(app)

DATABASE = 'student_app.db'

def get_db_connection():
    conn = sqlite3.connect(DATABASE)
    conn.row_factory = sqlite3.Row
    return conn

def initialize_database():
    if not os.path.exists(DATABASE):
        print("✅ student_app.db and tables created.")
        conn = get_db_connection()
        cursor = conn.cursor()
        cursor.execute('''
            CREATE TABLE students (
                student_id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL
            );
        ''')
        cursor.execute('''
            CREATE TABLE books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                branch TEXT,
                year INTEGER,
                available INTEGER DEFAULT 1
            );
        ''')
        cursor.execute('''
            CREATE TABLE stationery (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                item_name TEXT NOT NULL,
                description TEXT,
                category TEXT
            );
        ''')
        cursor.execute('''
            CREATE TABLE borrowed_books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                student_id INTEGER NOT NULL,
                book_id INTEGER NOT NULL,
                borrow_date DATE,
                FOREIGN KEY(student_id) REFERENCES students(student_id),
                FOREIGN KEY(book_id) REFERENCES books(id)
            );
        ''')
        cursor.execute('''
            CREATE TABLE payments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                student_id INTEGER NOT NULL,
                amount REAL,
                payment_date DATE,
                FOREIGN KEY(student_id) REFERENCES students(student_id)
            );
        ''')
        conn.commit()
        conn.close()

def import_books():
    path = os.path.join(os.path.dirname(__file__), 'books.csv')
    conn = get_db_connection()
    cursor = conn.cursor()
    with open(path, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            cursor.execute('''
                INSERT INTO books (title, author, branch, year, available)
                VALUES (?, ?, ?, ?, ?)
            ''', (row['title'], row['author'], row['branch'], row['year'], row['available']))
    conn.commit()
    conn.close()
    print("📚 Imported books")

def import_stationery():
    path = os.path.join(os.path.dirname(__file__), 'stationery.csv')
    conn = get_db_connection()
    cursor = conn.cursor()
    with open(path, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            cursor.execute('''
                INSERT INTO stationery (item_name, description, category)
                VALUES (?, ?, ?)
            ''', (row['item_name'], row['description'], row['category']))
    conn.commit()
    conn.close()
    print("✏️ Imported stationery items")

@app.route('/')
def home():
    return "✅ Student Management API is running"

@app.route('/student_login', methods=['POST'])
def student_login():
    data = request.get_json()
    identifier = data.get('student_id') or data.get('email')
    password = data.get('password')

    if not identifier or not password:
        return jsonify({"message": "Missing credentials"}), 400

    conn = get_db_connection()
    cursor = conn.cursor()

    # Try login by student_id
    cursor.execute("SELECT * FROM students WHERE student_id = ? AND password = ?", (identifier, password))
    student = cursor.fetchone()

    if not student:
        # Try login by email
        cursor.execute("SELECT * FROM students WHERE email = ? AND password = ?", (identifier, password))
        student = cursor.fetchone()

    conn.close()

    if student:
        access_token = create_access_token(identity=student['student_id'])
        return jsonify({
            "message": "Login successful",
            "student_id": student['student_id'],
            "name": student['name'],
            "email": student['email'],
            "access_token": access_token
        }), 200
    else:
        return jsonify({"message": "Invalid student ID/email or password"}), 401

@app.route('/get_books', methods=['GET'])
@jwt_required()
def get_books():
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM books")
    books = cursor.fetchall()
    conn.close()
    return jsonify([dict(row) for row in books])

@app.route('/get_stationery', methods=['GET'])
def get_stationery():
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM stationery")
    items = cursor.fetchall()
    conn.close()
    return jsonify([dict(row) for row in items])

@app.route('/borrow_book', methods=['POST'])
@jwt_required()
def borrow_book():
    data = request.get_json()
    student_id = get_jwt_identity()
    book_id = data.get('book_id')

    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT available FROM books WHERE id = ?", (book_id,))
    book = cursor.fetchone()

    if book and book["available"]:
        cursor.execute("INSERT INTO borrowed_books (student_id, book_id, borrow_date) VALUES (?, ?, DATE('now'))", (student_id, book_id))
        cursor.execute("UPDATE books SET available = 0 WHERE id = ?", (book_id,))
        conn.commit()
        conn.close()
        return jsonify({"message": "Book borrowed successfully"}), 200
    else:
        conn.close()
        return jsonify({"message": "Book not available"}), 400

@app.route('/return_book', methods=['POST'])
@jwt_required()
def return_book():
    data = request.get_json()
    student_id = get_jwt_identity()
    book_id = data.get('book_id')

    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("DELETE FROM borrowed_books WHERE student_id = ? AND book_id = ?", (student_id, book_id))
    cursor.execute("UPDATE books SET available = 1 WHERE id = ?", (book_id,))
    conn.commit()
    conn.close()
    return jsonify({"message": "Book returned successfully"}), 200

@app.route('/submit_payment', methods=['POST'])
@jwt_required()
def submit_payment():
    data = request.get_json()
    student_id = get_jwt_identity()
    amount = data.get('amount')

    if not amount:
        return jsonify({"message": "Amount is required"}), 400

    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("INSERT INTO payments (student_id, amount, payment_date) VALUES (?, ?, DATE('now'))", (student_id, amount))
    conn.commit()
    conn.close()
    return jsonify({"message": "Payment submitted successfully"}), 200

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)

