package dk.kea.webshopdat22b.controller;

import dk.kea.webshopdat22b.model.Product;
import dk.kea.webshopdat22b.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private ProductRepository productRepository;

    public HomeController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("products", productRepository.getAll());
        return "index";
    }

    //fra anchor i index
    @GetMapping("/create")
    public String showCreate(){
        //vis create-siden
        return "create";
    }

    //fra form action
    @PostMapping("/create")
    public String createProduct(@RequestParam("product-name") String newName,
                                @RequestParam("product-price") double newPrice){
        //lave et nyt Product
        Product newProduct = new Product();
        newProduct.setPrice(newPrice);
        newProduct.setName(newName);

        //gem nyt produkt
        productRepository.addProduct(newProduct);

        //tilbage til produktlisten
        return "redirect:/";
    }

    //vis update side for produkt ud fra parameter i url
    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") int updateId, Model model) {
        //find produkt med id=updateId i databasen
        Product updateProduct = productRepository.findProductById(updateId);

        //tilføj produkt til viewmodel, så det kan bruges i Thymeleaf
        model.addAttribute("product", updateProduct);

        //fortæl Spring hvilken HTML-side, der skal vises
        return "update";
    }

    @PostMapping("/update")
    public String updateProduct(@RequestParam("product-name") String updateName,
                                @RequestParam("product-price") double updatePrice,
                                @RequestParam("product-id") int updateId){
        //lav produkt ud fra parametre
        Product updateProduct = new Product(updateId, updateName, updatePrice);

        //kald opdater i repository
        productRepository.updateProduct(updateProduct);

        //rediriger til oversigtssiden
        return "redirect:/";
    }



}
