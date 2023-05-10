package com.example.ultrasoft.utility

class AppConstants {
    enum class AlertResponseType { YES, NO }
    enum class AlertType { SUCCESS, ERROR, INFO }
    enum class ComplaintStatus { INPROCESS, RESOLVED }


    companion object {
        const val ymSecret = "yourmudrasuperadmin"
        const val ymId = "yourmudrasuperadmin"
        const val WALLET_IMAGES_URL = "https://images.yourmudra.com/"
        const val EMP_IMAGES_URL = ""
        const val NODE_IMAGE_URL = "https://liveapionboarding.yourmudra.com/"
        private const val WALLET_UAT = "https://walletuat.yourmudra.com/v1/"
        private const val EMP_UAT = "https://employeeuat.yourmudra.com/"
//        private const val NODE_UAT = "https://uatapionboarding.yourmudra.com/v1/"

        private const val WALLET_PROD = " https://ymapi.yourmudra.com/YourmudraProd-1.1/v1/"
//        private const val NODE_PROD = "https://liveapionboarding.yourmudra.com/v1/"
        private const val EMP_PROD = "https://ymapi.yourmudra.com/ymemployee/"
        const val BaseWalletUrl = WALLET_UAT

    }

}