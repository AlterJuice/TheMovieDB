package exceptions

class NoSuchPropertyFileException(
    missedFileName: String,
    correspondingPath: String,
) : Exception("Provide the missed property file '${missedFileName}' to the corresponding path '${correspondingPath}'.")