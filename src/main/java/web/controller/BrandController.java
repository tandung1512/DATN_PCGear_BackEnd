package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import web.model.Brand;
import web.service.BrandService;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Operation(summary = "Get all brands")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved brands"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    public List<Brand> getAllBrands() {
        return brandService.getAllBrands();
    }

    @Operation(summary = "Get brand by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved brand"),
        @ApiResponse(responseCode = "404", description = "Brand not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{id}")
    public Brand getBrandById(@PathVariable String id) {
        return brandService.getBrandById(id);
    }

    @Operation(summary = "Save a brand")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Brand successfully created"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Brand createBrand(@RequestBody Brand brand) {
        return brandService.saveBrand(brand);
    }

    @Operation(summary = "Delete a brand by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted brand"),
        @ApiResponse(responseCode = "404", description = "Brand not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable String id) {
        brandService.deleteBrand(id);
    }
}
