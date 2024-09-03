package practice.kafka.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import practice.kafka.model.MyCdcModel;
import practice.kafka.service.MyService;

import java.util.List;

/**
 * 기본적인 CRUD
 * HTTP STATUS와 같은 디테일한 구성은 생략
 */
@RestController
@RequiredArgsConstructor
public class MyCdcController {

    private final MyService myService;

    @GetMapping("/greetings")
    List<MyCdcModel> list() {
        return myService.findAll();
    }

    @GetMapping("/greetings/{id}")
    MyCdcModel get(
            @PathVariable Integer id
    ) {
        return myService.findById(id);
    }

    @PostMapping("/greetings")
    MyCdcModel greeting(@RequestBody Request request) {
        MyCdcModel myCdcModel = MyCdcModel.of(
                request.userId,
                request.userAge,
                request.userName,
                request.content
        );
        return myService.save(myCdcModel);
    }

    @PatchMapping("/greetings/{id}")
    MyCdcModel update(
            @PathVariable Integer id,
            @RequestBody String content
    ) {
        return myService.update(id, content);
    }

    @DeleteMapping("/greetings/{id}")
    void delete(
            @PathVariable Integer id
    ) {
        myService.delete(id);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Request {
        Integer userId;
        String userName;
        Integer userAge;
        String content;
    }
}
