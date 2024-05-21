
export function etagFixer(eTagValue: String) {
  if (eTagValue.startsWith("W/")) {
    return(eTagValue.slice(2))

  }

}