my $VERSION = 0.01;

{
    my $AUTHORITY = 'cpan:STEVAN';
}

package Geometry {

    sub create_path ( $x1, $y1, $x2, $y2 ) {
        [ Point.new( $x1, $y1 ), Point.new( $x2, $y2 ) ];
    }

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

    class Point3D extends Point {
        has $.z;

        BUILD ( $x, $y, $z ) {
            super( $x, $y );
            $.z = $z;
        }

        method clear () {
            super();
            $.z = 0;
        }
    }

}