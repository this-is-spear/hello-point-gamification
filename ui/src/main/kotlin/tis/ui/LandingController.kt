package tis.ui

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class LandingController {
    @GetMapping
    fun index(): String = "index"
}
