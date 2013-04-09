package Test::More {

    my $test_count = 0;

    sub plan ($count) is export {
        say 1, "..", $count;
    }

    sub done_testing is export {
        say 1, "..", $test_count;
    }

    # NOTE:
    # The chr(35) silliness is because 
    # our comment parser seems to like
    # to sometimes gobble up quoted strings
    # and again, I can't figure it out 
    # so I added in chr() for now.
    # - SL

    sub ok ($test, $msg?) is export {
        $test_count = $test_count + 1;
        if ($test) {
            say [ "ok", $test_count, ($msg || "") ].join(" "); 
        } else {
            say [ "not ok", $test_count, ($msg || "") ].join(" ");
        }
    }

    sub is ($got, $expected, $msg?) is export {
        $test_count = $test_count + 1;

        my $result;
        if ($got.isa("Str")) {
            $result = $got eq ~$expected;   
        } elsif ($got.isa("Int") || $got.isa("Num")) {
            $result = $got == +$expected;
        } else {
            die("Can only compare Str, Int and Num objects");
        }

        if ($result) {
            say [ "ok", $test_count, ($msg || "") ].join(" "); 
        } else {
            say [ "not ok", $test_count, ($msg || "") ].join(" ");
            warn( 
                "#  Failed test", ($msg || ""),
                "\n",
                "#    got:      ", ~$got, 
                "\n", 
                "#    expected: ", ~$expected
            );
        }
    }

}
