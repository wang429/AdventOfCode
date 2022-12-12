
use std::fs::File;
use std::io::{BufReader, BufRead};

pub fn day1() {
    let test: Vec<&str> = vec![
        "1000",
        "2000",
        "3000",
        "",
        "4000",
        "",
        "5000",
        "6000",
        "",
        "7000",
        "8000",
        "9000",
        "",
        "10000"
    ];

    let filename = ".\\src\\main\\resources\\jwang\\y2022\\Day1Input.txt";
    let file = File::open(filename).unwrap();
    let reader = BufReader::new(file);

    let mut max = 0;
    let mut sum = 0;
    for result in reader.lines() {
        let line : String = result.unwrap();
    // for line in test {
        println!("{:?}", line);
        if line.is_empty() {
            if sum > max {
                max = sum;
                sum = 0;
            }
        }
        else {
            sum += line.parse::<i32>().unwrap();
        }
    }
    println!("{:?}", max);
}