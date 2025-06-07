package exceptions

class NoSuchPropertyFieldException(
    missedPropertyField: String
) : Exception("Provide the missed property '${missedPropertyField}' to the corresponding properties file.")