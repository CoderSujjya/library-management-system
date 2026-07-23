package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/welcome")
    public String welcome(HttpSession session, Model model) {
        model.addAttribute("username", session.getAttribute("loggedInUser"));
        return "welcome";
    }

    @GetMapping("/books/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/books/list";
    }

    @GetMapping("/books/list")
    public String listBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "book-list";
    }

    @GetMapping("/books/{id}")
    public String bookDetails(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return "redirect:/books/list";
        }
        model.addAttribute("book", book);
        return "book-details";
    }

    @GetMapping("/books/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return "redirect:/books/list";
        }
        model.addAttribute("book", book);
        return "update-book";
    }

    @PostMapping("/books/update")
    public String updateBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/books/list";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books/list";
    }
}
