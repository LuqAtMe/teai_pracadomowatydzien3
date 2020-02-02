package home.application.teai_pracadomowatydzien3;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarApi {

    private List<Car> carList;

    public CarApi() {
        this.carList = new ArrayList<Car>();
        carList.add(new Car(1, "BMW", "M5", "Blue"));
        carList.add(new Car(2, "Mercedes-Benz", "AMG A320", "Orange"));
        carList.add(new Car(3, "Lexus", "IS300H", "Red"));
    }

    //do pobierania wszystkich pozycji
    @GetMapping (produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Car>> getCarList() {
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    //do pobierania elementu po jego id
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable int id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId().equals(id)).findFirst();
        if (first.isPresent()) {
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //do pobierania elementów w określonym kolorze (query)
    @GetMapping("color/{color}")
    public ResponseEntity<List<Car>> getCarsByColor(@PathVariable String color) {
        List<Car> carListColor = new ArrayList<>();
        carList.stream().filter(car -> car.getColor().equals(color)).forEach(carListColor::add);
        if (!carListColor.isEmpty()) {
            return new ResponseEntity<>(carListColor, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //do dodawania pozycji
    @PostMapping
    public ResponseEntity<Car> newCar(@RequestBody Car newCar) {
        boolean flag = carList.add(newCar);
        if (flag) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //do modyfikowania pozycji
    @PutMapping
    public ResponseEntity<Car> updateCar(@RequestBody Car updateCar) {
        Optional<Car> carToRemove = carList.stream().filter(car -> car.getId() == updateCar.getId()).findFirst();
        if (carToRemove.isPresent()) {
            carList.remove(carToRemove.get());
            carList.add(updateCar);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //do modyfikowania jednego z pól pozycji
    @PatchMapping("/{id}")
    public ResponseEntity<Car> updateCarParameter(@PathVariable int id, @RequestHeader String color) {

        Optional<Car> first = carList.stream().filter(car -> car.getId().equals(id)).findFirst();
        if (first.isPresent()) {
            carList.stream().filter(car -> car.getId().equals(id)).findFirst().get().setColor(color);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //do usuwania jeden pozycji
    @DeleteMapping("/{id}")
    public ResponseEntity<Car> deleteCar(@PathVariable int id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId().equals(id)).findFirst();
        if (first.isPresent()) {
            carList.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
