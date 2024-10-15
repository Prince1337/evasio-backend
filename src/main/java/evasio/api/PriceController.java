package evasio.api;


import com.stripe.exception.StripeException;
import evasio.dto.PriceDTO;
import evasio.services.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prices")
@CrossOrigin(originPatterns = "http://localhost:4200")
public class PriceController {

    @Autowired
    private PriceService priceService;



    @GetMapping("/{id}")
    public ResponseEntity<PriceDTO> findById(@PathVariable String id) {
        Optional<PriceDTO> price = priceService.findById(id);
        return price.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<PriceDTO> add(@RequestBody PriceDTO priceDTO) throws StripeException {
        System.out.println("priceDTO: " + priceDTO);
        PriceDTO savedPrice = priceService.save(priceDTO);
        return new ResponseEntity<>(savedPrice, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriceDTO> update(@PathVariable String id, @RequestBody PriceDTO priceDTO) throws StripeException {
        priceDTO.setId(id);
        PriceDTO updatedPrice = priceService.save(priceDTO);
        return new ResponseEntity<>(updatedPrice, HttpStatus.OK);
    }
}
