use Test::More;

ok(not(false), '... not works properly');

my $x;
ok(not($x.defined), '... not works on methods too');

done_testing();