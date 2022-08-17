package abhi.example.hp.stenobano.retrofit;

import com.google.gson.JsonObject;


import java.util.List;

import abhi.example.hp.stenobano.chat.notification_model.RootModel;
import abhi.example.hp.stenobano.model.ModelAds;
import abhi.example.hp.stenobano.model.ModelCategory;
import abhi.example.hp.stenobano.model.ModelCategoryDetail;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {


    @Multipart
    @POST("getCategory.php")
    Call<ModelCategory> getCategory(
            @Part("key") RequestBody key
    );

    @Multipart
    @POST("get_ads.php")
    Call<ModelAds>get_ads(
            @Part("key") RequestBody key
    );

    @Multipart
    @POST("getDataAudio_Image.php")
    Call<ModelCategoryDetail> getDataAudio_Image(
            @Part("id") RequestBody id
    );

    @Multipart
    @POST("notification/startlive_notification.php")
    Call<JsonObject> startlive_notification(
            @Part("sender_id") RequestBody user_id,
            @Part("school_id") RequestBody school_id,
            @Part("title") RequestBody title,
            @Part("body") RequestBody body,
            @Part("topic") RequestBody topic,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("subject") RequestBody subject
    );

    @Multipart
    @POST("api.php?p=joinroombyuser")
    Call<JsonObject> joinroombyuser(
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id,
            @Part("student_id") RequestBody student_id,
            @Part("room_id") RequestBody room_id,
            @Part("type") RequestBody type

    );


    @Multipart
    @POST("api.php?p=updateLiveStatus")
    Call<JsonObject> updateLiveStatus(
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id,
            @Part("student_id") RequestBody student_id,
            @Part("room_id") RequestBody room_id,
            @Part("status") RequestBody status
    );

    @Multipart
    @POST("api.php?p=updateStudentLiveStatus")
    Call<JsonObject> updateStudentLiveStatus(
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id,
            @Part("student_id") RequestBody student_id,
            @Part("room_id") RequestBody room_id,
            @Part("status") RequestBody status
    );


    @Multipart
    @POST("update_token.php")
    Call<JsonObject>update_token(
            @Part("user_id") RequestBody user_id,
            @Part("token") RequestBody token,
            @Part("type") RequestBody type
    );

    @Multipart
    @POST("api.php?p=removeuserfromroom")
    Call<JsonObject> removeuserfromroom(
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id,
            @Part("student_id") RequestBody student_id,
            @Part("room_id") RequestBody room_id,
            @Part("type") RequestBody type

    );

    @Headers({"Authorization: key=" + "AAAAokNr_4I:APA91bGRMjunsGfW9UXeLrHZ8UWtPI-4dx5NFxIAC4Qbelha_h7T6ygive7xSycBIIoSlF5HDGnLENwmeYCc_E9niCIXHjolUe4yt7tsP6ZOpW6vDO4WxpzI2ZANfj32BNNIu29GkvFO", "Content-Type:application/json"})
    @POST("fcm/send")
    Call<JsonObject> sendNotification(@Body RootModel root);


    @Multipart
    @POST("notification/sendNotification.php")
    Call<JsonObject> sendNotification(
            @Part("sender_id") RequestBody user_id,
            @Part("school_id") RequestBody school_id,
            @Part("title") RequestBody title,
            @Part("body") RequestBody body,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id
    );


    @Multipart
    @POST("sign_upas_provider")
    Call<JsonObject> sign_upas_provider(@Part("name") RequestBody name,
                                        @Part("lat") RequestBody CompCod,
                                        @Part("lon") RequestBody CentCode,
                                        @Part("mobile") RequestBody TypeCode,
                                        @Part("cat_id") RequestBody CityCode,
                                        @Part("amt") RequestBody ClientCode,
                                        @Part("state") RequestBody ClientName,
                                        @Part("city") RequestBody Mobile,
                                        @Part("address") RequestBody Addr1,
                                        @Part MultipartBody.Part PhotoPath);






    @Multipart
    @POST("api.php?p=updateProfilePic")
    Call<JsonObject> updateProfilePic(@Part("login_id") RequestBody login_id,
                                      @Part("file_name") RequestBody file_name,
                                      @Part("type") RequestBody type,
                                      @Part MultipartBody.Part profilepath);


    @Multipart
    @POST("api.php?p=addstudent")
    Call<JsonObject> addstudent(@Part("first_name") RequestBody first_name,
                                @Part("last_name") RequestBody last_name,
                                @Part("classname") RequestBody class_name,
                                @Part("class_id") RequestBody class_id,
                                @Part("section") RequestBody section,
                                @Part("section_id") RequestBody section_id,
                                @Part("school_id") RequestBody school_id,
                                @Part("created_by_id") RequestBody created_by_id,
                                @Part("created_by") RequestBody created_by,
                                @Part("pname") RequestBody pname,
                                @Part("email") RequestBody email,
                                @Part("primary_mobile") RequestBody primary_mobile,
                                @Part("secondry_mobile") RequestBody secondry_mobile,
                                @Part("gender") RequestBody gender,
                                @Part MultipartBody.Part profilepath);


    @Multipart
    @POST("api.php?p=updateStudent")
    Call<JsonObject> updateStudent(@Part("first_name") RequestBody first_name,
                                   @Part("last_name") RequestBody last_name,
                                   @Part("classname") RequestBody class_name,
                                   @Part("class_id") RequestBody class_id,
                                   @Part("section") RequestBody section,
                                   @Part("section_id") RequestBody section_id,
                                   @Part("school_id") RequestBody school_id,
                                   @Part("created_by_id") RequestBody created_by_id,
                                   @Part("created_by") RequestBody created_by,
                                   @Part("pname") RequestBody pname,
                                   @Part("email") RequestBody email,
                                   @Part("primary_mobile") RequestBody primary_mobile,
                                   @Part("secondry_mobile") RequestBody secondry_mobile,
                                   @Part("gender") RequestBody gender,
                                   @Part("old_mobile") RequestBody old_mobile,
                                   @Part("login_id") RequestBody login_id,
                                   @Part("student_login") RequestBody student_login,
                                   @Part("img_name") RequestBody img_name,
                                   @Part("address") RequestBody address,
                                   @Part MultipartBody.Part profilepath);




    @Multipart
    @POST("api.php?p=addstudent")
    Call<JsonObject> addstudent2(@Part("first_name") RequestBody first_name,
                                 @Part("last_name") RequestBody last_name,
                                 @Part("classname") RequestBody class_name,
                                 @Part("class_id") RequestBody class_id,
                                 @Part("section") RequestBody section,
                                 @Part("section_id") RequestBody section_id,
                                 @Part("school_id") RequestBody school_id,
                                 @Part("created_by_id") RequestBody created_by_id,
                                 @Part("created_by") RequestBody created_by,
                                 @Part("pname") RequestBody pname,
                                 @Part("email") RequestBody email,
                                 @Part("primary_mobile") RequestBody primary_mobile,
                                 @Part("secondry_mobile") RequestBody secondry_mobile,
                                 @Part("gender") RequestBody gender);

    @Multipart
    @POST("api.php?p=updateStudent")
    Call<JsonObject> updateStudent2(@Part("first_name") RequestBody first_name,
                                    @Part("last_name") RequestBody last_name,
                                    @Part("classname") RequestBody class_name,
                                    @Part("class_id") RequestBody class_id,
                                    @Part("section") RequestBody section,
                                    @Part("section_id") RequestBody section_id,
                                    @Part("school_id") RequestBody school_id,
                                    @Part("created_by_id") RequestBody created_by_id,
                                    @Part("created_by") RequestBody created_by,
                                    @Part("pname") RequestBody pname,
                                    @Part("email") RequestBody email,
                                    @Part("primary_mobile") RequestBody primary_mobile,
                                    @Part("secondry_mobile") RequestBody secondry_mobile,
                                    @Part("gender") RequestBody gender,
                                    @Part("old_mobile") RequestBody old_mobile,
                                    @Part("login_id") RequestBody login_id,
                                    @Part("student_login") RequestBody student_login,
                                    @Part("address") RequestBody address
    );







    @Multipart
    @POST("api.php?p=addstudent_ifexist")
    Call<JsonObject> addstudent_ifexist(@Part("first_name") RequestBody first_name,
                                        @Part("last_name") RequestBody last_name,
                                        @Part("classname") RequestBody class_name,
                                        @Part("class_id") RequestBody class_id,
                                        @Part("section_id") RequestBody section_id,
                                        @Part("section") RequestBody section,
                                        @Part("school_id") RequestBody school_id,
                                        @Part("created_by_id") RequestBody created_by_id,
                                        @Part("created_by") RequestBody created_by,
                                        @Part("pname") RequestBody pname,
                                        @Part("email") RequestBody email,
                                        @Part("primary_mobile") RequestBody primary_mobile,
                                        @Part("secondry_mobile") RequestBody secondry_mobile,
                                        @Part("gender") RequestBody gender,
                                        @Part MultipartBody.Part profilepath);

    @Multipart
    @POST("api.php?p=updateStudent_ifexist")
    Call<JsonObject> updateStudent_ifexist(@Part("first_name") RequestBody first_name,
                                           @Part("last_name") RequestBody last_name,
                                           @Part("classname") RequestBody class_name,
                                           @Part("class_id") RequestBody class_id,
                                           @Part("section_id") RequestBody section_id,
                                           @Part("section") RequestBody section,
                                           @Part("school_id") RequestBody school_id,
                                           @Part("created_by_id") RequestBody created_by_id,
                                           @Part("created_by") RequestBody created_by,
                                           @Part("pname") RequestBody pname,
                                           @Part("email") RequestBody email,
                                           @Part("primary_mobile") RequestBody primary_mobile,
                                           @Part("secondry_mobile") RequestBody secondry_mobile,
                                           @Part("address") RequestBody address,
                                           @Part("gender") RequestBody gender,
                                           @Part("img_name") RequestBody imgname,
                                           @Part("login_id") RequestBody login_id,
                                           @Part("student_id") RequestBody student_id,
                                           @Part MultipartBody.Part profilepath);

    @Multipart
    @POST("api.php?p=updateStudent_ifexist")
    Call<JsonObject> updateStudent_ifexist2(@Part("first_name") RequestBody first_name,
                                            @Part("last_name") RequestBody last_name,
                                            @Part("classname") RequestBody class_name,
                                            @Part("class_id") RequestBody class_id,
                                            @Part("section_id") RequestBody section_id,
                                            @Part("section") RequestBody section,
                                            @Part("school_id") RequestBody school_id,
                                            @Part("created_by_id") RequestBody created_by_id,
                                            @Part("created_by") RequestBody created_by,
                                            @Part("pname") RequestBody pname,
                                            @Part("email") RequestBody email,
                                            @Part("primary_mobile") RequestBody primary_mobile,
                                            @Part("secondry_mobile") RequestBody secondry_mobile,
                                            @Part("address") RequestBody address,
                                            @Part("gender") RequestBody gender,
                                            @Part("img_name") RequestBody imgname,
                                            @Part("login_id") RequestBody login_id,
                                            @Part("student_id") RequestBody student_id);



    @Multipart
    @POST("api.php?p=addstudent_ifexist")
    Call<JsonObject> addstudent_ifexist2(@Part("first_name") RequestBody first_name,
                                         @Part("last_name") RequestBody last_name,
                                         @Part("classname") RequestBody class_name,
                                         @Part("class_id") RequestBody class_id,
                                         @Part("section_id") RequestBody section_id,
                                         @Part("section") RequestBody section,
                                         @Part("school_id") RequestBody school_id,
                                         @Part("created_by_id") RequestBody created_by_id,
                                         @Part("created_by") RequestBody created_by,
                                         @Part("pname") RequestBody pname,
                                         @Part("email") RequestBody email,
                                         @Part("primary_mobile") RequestBody primary_mobile,
                                         @Part("secondry_mobile") RequestBody secondry_mobile);






    @Multipart
    @POST("api.php?p=getclass")
    Call<JsonObject> getclasss(@Part("school_id") RequestBody school_id);

    @Multipart
    @POST("api.php?p=deleteRooms")
    Call<JsonObject> deleteRooms(@Part("room_id") RequestBody room_id);




    @Multipart
    @POST("api.php?p=deleteschool")
    Call<JsonObject> deleteschool(@Part("school_id") RequestBody school_id);

    @Multipart
    @POST("api.php?p=updateclass_section")
    Call<JsonObject> updateclass_section(@Part("school_id") RequestBody school_id,
                                         @Part("id") RequestBody id,
                                         @Part("update_value") RequestBody update_value,
                                         @Part("old_value") RequestBody old_value,
                                         @Part("type") RequestBody type);
    @Multipart
    @POST("api.php?p=deleteclass_section")
    Call<JsonObject> deleteclass_section(@Part("school_id") RequestBody school_id,
                                         @Part("id") RequestBody id,
                                         @Part("type") RequestBody type);

    @Multipart
    @POST("api.php?p=create_class_teacher")
    Call<JsonObject> create_class_teacher(@Part("school_id") RequestBody school_id,
                                          @Part("user_id") RequestBody user_id,
                                          @Part("teacher_id") RequestBody teacher_id,
                                          @Part("class_id") RequestBody class_id,
                                          @Part("section_id") RequestBody section_id);
    @Multipart
    @POST("api.php?p=updateClassTeacher")
    Call<JsonObject> updateClassTeacher
                                          (@Part("school_id") RequestBody school_id,
                                           @Part("user_id") RequestBody user_id,
                                           @Part("teacher_id") RequestBody teacher_id,
                                           @Part("class_id") RequestBody class_id,
                                           @Part("section_id") RequestBody section_i,
                                           @Part("add") RequestBody add,
                                           @Part("exist_id") RequestBody exist_id,
                                           @Part("pre_id") RequestBody pre_id);


    @Multipart
    @POST("api.php?p=deleteTeacher")
    Call<JsonObject> deleteTeacher(@Part("school_id") RequestBody school_id,
                                   @Part("user_id") RequestBody user_id);

    @Multipart
    @POST("api.php?p=deleteClassTeacher")
    Call<JsonObject> deleteClassTeacher(@Part("school_id") RequestBody school_id,
                                        @Part("id") RequestBody user_id);





    @Multipart
    @POST("api.php?p=changepassword")
    Call<JsonObject> changepassword(@Part("user_id") RequestBody school_id,
                                    @Part("old_pass") RequestBody old_pass,
                                    @Part("new_pass") RequestBody new_pass
    );




    @Multipart
    @POST("api.php?p=upload_homework")
    Call<JsonObject> upload_homework(
            @Part List<MultipartBody.Part> gallary,
            @Part MultipartBody.Part file,
            @Part("student_id") RequestBody std_id,
            @Part("school_id") RequestBody school_id,
            @Part("session_id") RequestBody session_id,
            @Part("assignment_id") RequestBody assignment_id,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("teacher_id") RequestBody teacherid,
            @Part("description") RequestBody description
    );

    @Multipart
    @POST("api.php?p=upload_homework")
    Call<JsonObject> upload_homework2(
            @Part MultipartBody.Part file,
            @Part("student_id") RequestBody std_id,
            @Part("school_id") RequestBody school_id,
            @Part("session_id") RequestBody session_id,
            @Part("assignment_id") RequestBody assignment_id,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("teacher_id") RequestBody teacherid,
            @Part("description") RequestBody description
    );






    @Multipart
    @POST("api.php?p=uploadAssignment")
    Call<JsonObject> uploadAssignment(
            @Part MultipartBody.Part file,
            @Part("subject") RequestBody subject,
            @Part("topic") RequestBody topic,
            @Part("des") RequestBody des,
            @Part("date") RequestBody date,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("section_name") RequestBody section_name,
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id
    );




    @Multipart
    @POST("api.php?p=upload_homework")
    Call<JsonObject> upload_homework3(
            @Part List<MultipartBody.Part> gallary,
            @Part("student_id") RequestBody std_id,
            @Part("school_id") RequestBody school_id,
            @Part("session_id") RequestBody session_id,
            @Part("assignment_id") RequestBody assignment_id,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("teacher_id") RequestBody teacherid,
            @Part("description") RequestBody description
    );

    @Multipart
    @POST("api.php?p=upload_homework")
    Call<JsonObject> upload_homework4(

            @Part("student_id") RequestBody std_id,
            @Part("school_id") RequestBody school_id,
            @Part("session_id") RequestBody session_id,
            @Part("assignment_id") RequestBody assignment_id,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("teacher_id") RequestBody teacherid,
            @Part("description") RequestBody description
    );





    @Multipart
    @POST("api.php?p=deActiveStudent")
    Call<JsonObject> deActiveStudent(@Part("school_id") RequestBody school_id,
                                     @Part("student_id") RequestBody teacher_id,
                                     @Part("status") RequestBody type);

    @Multipart
    @POST("api.php?p=deleteStudent")
    Call<JsonObject> deleteStudent(@Part("user_id") RequestBody userid,
                                   @Part("school_id") RequestBody school_id,
                                   @Part("student_id") RequestBody student_id);


    @Multipart
    @POST("api.php?p=createRooms")
    Call<JsonObject> createRooms(@Part("school_id") RequestBody school_id,
                                 @Part("teacher_id") RequestBody teacher_id,
                                 @Part("class_id") RequestBody type,
                                 @Part("section_id") RequestBody section_id,
                                 @Part("section_name") RequestBody section_name,
                                 @Part("subject") RequestBody subject,
                                 @Part("title") RequestBody title,
                                 @Part("desc") RequestBody desc,
                                 @Part("time") RequestBody time);


    @Multipart
    @POST("api.php?p=uploadAssignment")
    Call<JsonObject> uploadAssignment(
            @Part("subject") RequestBody subject,
            @Part("topic") RequestBody topic,
            @Part("des") RequestBody des,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("section_name") RequestBody section_name,
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id);




    @Multipart
    @POST("api.php?p=updateRooms")
    Call<JsonObject> updateRooms(
            @Part("room_id") RequestBody room_id,
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id,
            @Part("class_id") RequestBody type,
            @Part("section_id") RequestBody section_id,
            @Part("section_name") RequestBody section_name,
            @Part("subject") RequestBody subject,
            @Part("title") RequestBody title,
            @Part("desc") RequestBody desc,
            @Part("time") RequestBody time);

}
