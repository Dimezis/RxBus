package rxbus.example

class Event {
    data class Text(var title: String)

    data class Counter(var count: Int)
}