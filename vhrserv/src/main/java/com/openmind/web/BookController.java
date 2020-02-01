package com.openmind.web;

import com.openmind.vo.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * BookController
 *
 * @author zhoujunwen
 * @date 2020-01-06
 * @time 22:01
 * @desc
 */
@Controller
public class BookController {
    @GetMapping("/books")
    public ModelAndView books() {
        List<Book> books = new ArrayList<>();
        Book b1 = new Book();
        b1.setAuthor("罗贯中");
        b1.setId(1);
        b1.setName("三国演义");

        Book b2 = new Book();
        b2.setAuthor("吴承恩");
        b2.setId(2);
        b2.setName("西游记");

        Book b3 = new Book();
        b3.setAuthor("曹雪芹");
        b3.setId(3);
        b3.setName("红楼梦");

        books.add(b1);
        books.add(b2);
        books.add(b3);

        ModelAndView mv = new ModelAndView();
        mv.addObject("books", books);
        mv.setViewName("books");
        return mv;
    }

    @GetMapping("/book")
    @ResponseBody
    public Book book() throws CloneNotSupportedException {
        Book b1 = new Book();
        b1.setAuthor("罗贯中");
        b1.setId(1);
        b1.setName("三国演义");

        Book b2 = (Book) b1.clone();

        return b2;
    }


}
