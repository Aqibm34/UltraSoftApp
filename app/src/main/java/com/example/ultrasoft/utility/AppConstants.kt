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

        //        const val NODE_IMAGE_URL = "https://liveapionboarding.yourmudra.com/"
//        private const val WALLET_PROD = " https://ymapi.yourmudra.com/YourmudraProd-1.1/v1/"
//        private const val EMP_UAT = "https://employeeuat.yourmudra.com/"
//        private const val WALLET_UAT = "https://walletuat.yourmudra.com/v1/"
//        private const val NODE_UAT = "https://uatapionboarding.yourmudra.com/v1/"
//        private const val NODE_PROD = "https://liveapionboarding.yourmudra.com/v1/"
//        private const val EMP_PROD = "https://ymapi.yourmudra.com/ymemployee/"
        const val ATTACHMENT_URL = "https://complaints.ultrasoftsys.com/assets/"
        const val BASE_URL = "https://complaints.ultrasoftsys.com/"

        const val REPLY_COMPLAIN_URL = BASE_URL + "customer/reply/complaint"
        const val CUST_ALL_COMPLAIN_URL = BASE_URL + "customer/getall/complaint/pending/resolved"
        const val COMPLAIN_BY_ID_URL = BASE_URL + "customer/get/complaint/"

        const val ENG_REPLY_COMPLAIN_URL = BASE_URL + "engineer/reply/complaint"
        const val ENG_ALL_COMPLAIN_URL = BASE_URL + "engineer/get/all/complaint/pending/resolved"
        const val ENG_COMPLAIN_BY_ID_URL = BASE_URL + "engineer/get/eng/complaint/"

        const val ADMIN_ALL_COMPLAIN_URL = BASE_URL + "admin/getall/admin/pending/resolved/complaint"


    }

}