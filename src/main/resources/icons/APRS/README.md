# APRS Direct Symbols
The APRS Direct symbols is based on a combination of the version 1.1 specification and the version 1.2 specification from www.aprs.org (and on usages that we have found to be common).

## APRS symbol basics
A symbol definition is based on two value, the *symbol code* and the *symbol table*. The *symbol table* is / for all primary symbols and \ for all alternative symbols. If the symbol has an overlay the *symbol table* will be the overlay character (which may be 0-9 and A-Z). Possible value for the *symbol code* is all characters between ! and ~ (all ascii code between 33 and 126). 

## Symbol files
The APRS Direct symbols is provided as SVG files and PNG files.

### PNG files
The symbol files will be named symbol-X-Y.png, the X is the *symbol code* and the Y is the *symbol table*. Note that the X and Y will be the corresponding ASCII digit instead of the actual character. 

*Example symbol-62-47.png*
The primary symbol for a car, 62 is the ascii code for the character > and 47 is the ascii character for / (which is the character for the primary symbol table).

*Example symbol-62-92.png*
The alternative symbol for a car, 62 is the ascii code for the character > and 92 is the ascii character for \ (which is the character for the alternative symbol table).

### SVG files
The raw svg files are only available in primary version and in alternative version. In cases where it is another version used for overlay character, there is a third version.

The SVG files are named symbol-X-1.svg (primary version) and symbol-X-2.svg (alternative version), and in some cases we have a third version named symbol-X-3.svg (version used for overlay character). It may even exist some files for specific overlay characters (in that case it will be named symbol-X-Y.svg).

## Authors
* Per Qvarforth (per@qvarforth.se)

## License
APRS Direct Symbols (c) by Per Qvarforth
APRS Direct Symbols is licensed under a Creative Commons Attribution 4.0 International License.
You should have received a copy of the license along with this work. If not, see <http://creativecommons.org/licenses/by/4.0/>.

### Explanation
You may use the symbols freely as long as you include an attribution. It's enough if you mention somewhere that the symbols are provided by APRS Direct (www.aprsdirect.com).
