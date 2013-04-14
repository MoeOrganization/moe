package Test {

    class Builder {
        has $!count = 0;

        method plan ($count) {
            say 1, "..", $count;
        }

        method done_testing {
            say 1, "..", $!count;
        }

        method ok ($test, $msg?) {
            $!count = $!count + 1;
            if ($test) {
                say [ "ok", $!count, ($msg || "") ].join(" "); 
            } else {
                say [ "not ok", $!count, ($msg || "") ].join(" ");
            }
        }

        method is ($got, $expected, $msg?) {
            $!count = $!count + 1;
            if (self.compare($got, $expected)) {
                say [ "ok", $!count, ($msg || "") ].join(" "); 
            } else {
                say [ "not ok", $!count, ($msg || "") ].join(" ");
                warn( 
                    "#  Failed test", ($msg || ""), "\n",
                    "#    got:      ", ~$got,       "\n", 
                    "#    expected: ", ~$expected
                );
            }
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