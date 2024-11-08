package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import web.model.Distinctive;
import web.service.DistinctiveService;

import java.util.List;

@RestController
@RequestMapping("/api/distinctives")
public class DistinctiveController {

    @Autowired
    private DistinctiveService distinctiveService;

    @Operation(summary = "Get all distinctives", description = "Retrieve a list of all distinctives.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of distinctives"),
        @ApiResponse(responseCode = "204", description = "No distinctives found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Distinctive>> getAllDistinctives() {
        List<Distinctive> distinctives = distinctiveService.getAllDistinctives();
        if (distinctives.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(distinctives);
        }
        return ResponseEntity.ok(distinctives);
    }

    @Operation(summary = "Get distinctive by ID", description = "Retrieve details of a distinctive by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved distinctive"),
        @ApiResponse(responseCode = "404", description = "Distinctive not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getDistinctiveById(@PathVariable String id) {
        try {
            Distinctive distinctive = distinctiveService.getDistinctiveById(id);
            return ResponseEntity.ok(distinctive);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getReason()));
        }
    }

    @Operation(summary = "Add a new distinctive", description = "Create a new distinctive entry.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Distinctive created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/add")
    public ResponseEntity<?> addDistinctive(@RequestBody Distinctive distinctive) {
        try {
            Distinctive addedDistinctive = distinctiveService.addDistinctive(distinctive);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedDistinctive);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getReason()));
        }
    }

    @Operation(summary = "Update distinctive by ID", description = "Update details of a distinctive by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Distinctive updated successfully"),
        @ApiResponse(responseCode = "404", description = "Distinctive not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDistinctiveById(@PathVariable String id, @RequestBody Distinctive distinctive) {
        try {
            Distinctive updatedDistinctive = distinctiveService.updateDistinctiveById(id, distinctive);
            return ResponseEntity.ok(updatedDistinctive);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getReason()));
        }
    }

    @Operation(summary = "Delete distinctive by ID", description = "Delete a distinctive by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Distinctive deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Distinctive not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDistinctive(@PathVariable String id) {
        try {
            distinctiveService.deleteDistinctive(id);
            return ResponseEntity.ok("Distinctive deleted successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getReason()));
        }
    }

    // Error response class for handling errors in the API response
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
