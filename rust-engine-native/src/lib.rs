#[unsafe(no_mangle)]
pub extern "C" fn rust_engine_add(a: i64, b: i64) -> i64 {
    a.wrapping_add(b)
}

#[cfg(test)]
mod tests {
    use super::rust_engine_add;

    #[test]
    fn adds_numbers() {
        assert_eq!(rust_engine_add(20, 22), 42);
    }

    #[test]
    fn uses_java_long_overflow_semantics() {
        assert_eq!(rust_engine_add(i64::MAX, 1), i64::MIN);
    }
}
