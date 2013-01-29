class Point {

    has $.x = 0;
    has $.y = 0;

    BUILD ( $x, $y ) {
        $.x = $x;
        $.y = $y;
    }

    method clear () {
        $.x = 0;
        $.y = 0;
    }
}