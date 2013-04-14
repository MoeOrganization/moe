package Test {

    class TAP {

        method plan ($count) { say 1, "..", $count }

        method ok     ($count, $msg) { say [ "ok",     $count, ($msg || "") ].join(" ") }
        method not_ok ($count, $msg) { say [ "not ok", $count, ($msg || "") ].join(" ") }

        method diag (*@msg) { warn @msg.join }
    }

    class Builder {

        has $!output = ^Test::TAP.new;
        has $!count  = 0;

        method plan ($count) { $!output.plan($count)  }
        method done_testing  { $!output.plan($!count) }

        method ok ($test, $msg?) {
            self.inc_count;
            ($test) 
                ? $!output.ok($!count, $msg) 
                : $!output.not_ok($!count, $msg);
        }

        method is ($got, $expected, $msg?) {
            self.inc_count;
            if (self.compare($got, $expected)) {
                $!output.ok($!count, $msg);
            } else {
                $!output.not_ok($!count, $msg);
                $!output.output_err($got, $expected, $msg); 
            }
        }

        submethod inc_count { $!count = $!count + 1; }

        submethod output_err ($got, $expected, $msg) {
            $!output.diag( 
                "#  Failed test", ($msg || ""), "\n",
                "#    got:      ", ~$got,       "\n", 
                "#    expected: ", ~$expected
            );
        }

        submethod compare ($got, $expected) {
            if ($got.isa("Str")) {
                $got eq ~$expected;   
            } elsif ($got.isa("Int") || $got.isa("Num")) {
                $got == +$expected;
            } elsif ($got.isa("Bool")) {
                $got == ?$expected;
            } else {
                die("Can only compare Str, Int, Num and Bool objects");
            }
        }
    }

    package More {

        my $builder = ^Test::Builder.new;

        sub plan ($count) is export { $builder.plan($count) }
        sub done_testing  is export { $builder.done_testing }

        sub ok ($test, $msg?) is export {
            $builder.ok($test, $msg);
        }

        sub is ($got, $expected, $msg?) is export {
            $builder.is($got, $expected, $msg);
        }

    }

}