package domain.home

data class HomeAddress(val Address: String){

    companion object {

        fun createRandomAddress(addressPrefix: String ): HomeAddress{
            return HomeAddress(addressPrefix + (0..999).random() )
        }

     }
}