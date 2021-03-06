package com.student.junit;

import Student.StudentClass;
import com.student.cucumber.serenity.StudentSerenitySteps;
import com.student.testbase.TestBase;
import com.student.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@RunWith(SerenityRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

//In testng we can set priority how we need to test as if some data is created 1st and then verified then only it will pass
    /* otherwise it will not return  the value which is not yet created hence we need to change
    the same and put priority of creation 1st and then ecxtraction 2nd

    For that fixMethodOrder and set it as Ascending and then refactor the method name in ascending order
     */

public class CrudOperations extends TestBase {


    @Steps
    StudentSerenitySteps studentSerenitySteps;

    StudentClass studentClass = new StudentClass();

    static String firstName = "Priya";
    String arrays[] = {"java", "C++", "python"};
    List<String> listOfCourses = Arrays.asList(arrays);

    @Title("post a new student")
    @Test
    public void createStudent() {
        StudentClass studentClass = new StudentClass();
        studentClass.setFirstName(firstName);
        studentClass.setEmail(TestUtils.randomValue() + "@domain.com");
        studentClass.setLastName("Verma");
        studentClass.setCourses(listOfCourses);
        studentClass.setId(TestUtils.randomId());

        SerenityRest.given().contentType(ContentType.JSON).log().all().when().body(studentClass).then().statusCode(200);
        // made this code reusable and using through Steps
       /* studentSerenitySteps.createStudent(TestUtils.randomId(),
                firstName,
                "Verma",
                TestUtils.randomValue() + "@domain.com",
                listOfCourses)
                .statusCode(201);*/
    }

    @Title("verify extraction of data")
    @Test
    public void getStudent() {
        String p1 = "findAll{it.firsttName=='";
        String p2 = "'}.get(0)";

        //.rest is used to mention reports and logs


        HashMap<String, Object> value = SerenityRest.rest()
                .given()
                .log()
                .all()
                .get("/list")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .path(p1 + firstName);

        //  assertThat(value, (Matcher<? super HashMap<String, Object>>) hasValue(firstName));
        /* studentId =(int) value.get(id); */
    }

    @Title("update and verify data")
    @Test
    public void test003() {

        // a postr request

        firstName = firstName + "_updated";
        studentClass.setFirstName(firstName);
        studentClass.setEmail(TestUtils.randomValue() + "@domain.com");
        studentClass.setLastName("Verma");
        studentClass.setCourses(listOfCourses);
        studentClass.setId(TestUtils.randomId());

        SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON)
                .log()
                .all()
                .when()
                .body(studentClass)
                .put("/" + studentClass.getId())
                .then()
                .statusCode(200);
    }


    @Title("delete a student")
    @Test
    public void testt004() {
        SerenityRest.rest().given().when().delete("/" + studentClass.getId());

        SerenityRest.rest().given().when().get("/" + studentClass.getId()).then().log().all().statusCode(404);
    }
}
