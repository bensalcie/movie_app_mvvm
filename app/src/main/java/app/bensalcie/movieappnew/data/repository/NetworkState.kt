package app.bensalcie.movieappnew.data.repository


//typesafe class
enum class Status{
    RUNNING,
    SUCCESS,
    FAILED
}
class NetworkState(val status: Status,val msg:String) {
    //when we want something static
    companion object{

        val LOADED :NetworkState
        val LOADING :NetworkState
        val ERROR :NetworkState
        val ENDOFLIST :NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS,"Success")
            LOADING = NetworkState(Status.RUNNING,"Running")
            ERROR = NetworkState(Status.FAILED,"Something went wrong")
            ENDOFLIST = NetworkState(Status.FAILED,"You have reached the end")

        }


    }
}