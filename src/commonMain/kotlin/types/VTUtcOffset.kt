package types

data class VTUtcOffset(val positive: Boolean, val hour: Int, val minute: Int, val second: Int = 0) : ValueType
