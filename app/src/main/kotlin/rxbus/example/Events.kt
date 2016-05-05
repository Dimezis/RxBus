package rxbus.example

class Events {
    data class TextEvent(var title: String)

    data class CounterEvent(var count: Int)
}