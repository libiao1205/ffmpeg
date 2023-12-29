package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author libiao
 * @date 2023/12/13
 */
@Controller
public class HtmlController {

    @GetMapping("/")
    public String example() {
        return "/index";
    }

    public static void main(String[] args) {

        for (int i = 1; i <= 9; i++ ) {
            for (int j = i; j <= 9; j++) {
                System.out.print(i + "*" + j + "=" + (i*j) + "\t");
            }
            System.out.println();
        }
        System.out.println("\n");
        for (int i = 9; i >= 1; i-- ) {
            for (int j = i; j <= 9; j++) {
                System.out.print(i + "*" + j + "=" + (i*j) + "\t");
            }
            System.out.println();
        }
        System.out.println("\n");
        for (int i = 1; i <= 9; i++) {
            for (int j = 9; j >= i; j--) {
                System.out.print(i + "*" + j + "=" + (i*j) + "\t");
            }
            System.out.println();
            for (int c = 1; c <= i; c++) {
                System.out.print("\t\t");
            }
        }

    }
}
