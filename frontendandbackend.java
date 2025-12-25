package com.example.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

@SpringBootApplication
@RestController
public class LoginApplication {

    // ===== DATABASE CONFIG =====
    static final String DB_URL = "jdbc:mysql://localhost:3306/login_db";
    static final String DB_USER = "root";
    static final String DB_PASS = "YOUR_MYSQL_PASSWORD";

    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
    }

    // ===== FRONTEND (HTML + CSS + JS) =====
    @GetMapping("/")
    public String loginPage() {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Login Page</title>
            <style>
                body {
                    font-family: Arial;
                    background: #f2f2f2;
                }
                .box {
                    width: 300px;
                    margin: 150px auto;
                    background: white;
                    padding: 20px;
                    text-align: center;
                    box-shadow: 0 0 10px gray;
                }
                input {
                    width: 90%;
                    padding: 10px;
                    margin: 10px 0;
                }
                button {
                    width: 95%;
                    padding: 10px;
                    background: green;
                    color: white;
                    border: none;
                    cursor: pointer;
                }
            </style>
        </head>
        <body>

        <div class="box">
            <h2>Login</h2>
            <input type="text" id="username" placeholder="Username">
            <input type="password" id="password" placeholder="Password">
            <button onclick="login()">Login</button>
            <p id="msg"></p>
        </div>

        <script>
            function login() {
                let u = document.getElementById("username").value;
                let p = document.getElementById("password").value;

                fetch("/login", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: "username=" + u + "&password=" + p
                })
                .then(res => res.text())
                .then(data => {
                    document.getElementById("msg").innerText = data;
                });
            }
        </script>

        </body>
        </html>
        """;
    }

    // ===== BACKEND + DATABASE =====
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {

        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return "Login Successful ✅";
            } else {
                return "Invalid Username or Password ❌";
            }

        } catch (Exception e) {
            return "Database Error";
        }
    }
}
