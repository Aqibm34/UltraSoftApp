package com.example.ultrasoft.utility

class AppConstants {
    enum class AlertResponseType { YES, NO }
    enum class STATUS { ACTIVE, INACTIVE }
    enum class AlertType { SUCCESS, ERROR, INFO }
    enum class UserTypes { ADMIN, CUSTOMER, ENGINEER }
    enum class ComplaintStatus { UN_ASSIGNED, RESOLVED, IN_PROGRESS, CLOSED, PENDING }


    companion object {
        const val ymSecret = "yourmudrasuperadmin"
        const val ymId = "yourmudrasuperadmin"
        const val WALLET_IMAGES_URL = "https://images.yourmudra.com/"
        const val EMP_IMAGES_URL = ""

        const val ATTACHMENT_URL = "https://complaintsimages.ultrasoftsys.com/upload/"
        const val BASE_URL = "https://complaints.ultrasoftsys.com/"

        const val REPLY_COMPLAIN_URL = BASE_URL + "customer/reply/complaint"
        const val CUST_ALL_COMPLAIN_URL = BASE_URL + "customer/getall/complaint/bystatus"
        const val COMPLAIN_BY_ID_URL = BASE_URL + "customer/get/complaint/"
        const val CUSTOMER_COMPLAIN_COUNT_URL = BASE_URL + "customer/getall/count"
        const val CUST_CHANGE_PASS_URL = BASE_URL + "customer/change/password"

        const val ENG_REPLY_COMPLAIN_URL = BASE_URL + "engineer/reply/complaint"
        const val ENG_ALL_COMPLAIN_URL = BASE_URL + "engineer/get/all/complaint/status"
        const val ENG_COMPLAIN_BY_ID_URL = BASE_URL + "engineer/get/eng/complaint/"
        const val ENG_COMPLAIN_COUNT_URL = BASE_URL + "engineer/getall/count"
        const val ENG_CHANGE_PASS_URL = BASE_URL + "engineer/change/password"

        const val ADMIN_ALL_COMPLAIN_URL = BASE_URL + "admin/get/allcomplaint/status"
        const val ADMIN_REPLY_COMPLAIN_URL = BASE_URL + "admin/reply/admin/complaint"
        const val ADMIN_GET_COMPLAIN_BY_ID = BASE_URL + "admin/get/complaintId/"
        const val ADMIN_COMPLAIN_COUNT_URL = BASE_URL + "admin/getall/count"
        const val ADMIN_CHANGE_PASS_URL = BASE_URL + "admin/change/password"


    }

}