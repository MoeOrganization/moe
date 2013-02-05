if ( "true" ) {
    my @foo;
    for ( my $i = @foo; $i == 0; $i-- ) {
        while ( 1 ) {
            my $bar = "does it work this deeply nested?";
        }
    }
}