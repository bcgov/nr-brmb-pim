
// When running in OpenShift, Tomcat appears to sometimes incorrectly flag eTags as weak.
// As all eTags in-use are strong, this function is run on eTags in PUT/POST requests to 
// ensure saving changes is possible

export function etagFixer(eTagValue: String) {
  if (eTagValue != null &&eTagValue.startsWith("W/")) {
    return(eTagValue.slice(2))
    

  }

}