function encrypt(s, key) {
	s = s.toString().split('')
	for (var i = 0; i < s.length; i++) {
		s[i] = String.fromCharCode(s[i].charCodeAt() + key.charCodeAt(i % key.length))
	}
	s = s.join("")
	return (s)
}

function decrypt(s, key) {
	s = s.toString().split('')
	for (var i = 0; i < s.length; i++) {
		s[i] = String.fromCharCode(s[i].charCodeAt() - key.charCodeAt(i % key.length))
	}
	s = s.join("")
	return (s)
}

function generateKey(length) {
	var s = ""
	for (var i = 0; i < length; i++) {
		s+=String.fromCharCode(Math.floor(Math.random()*88))
	}
	return s
}
var key = generateKey(5)
var text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sed dui ipsum. Quisque sem turpis, vulputate sit amet blandit in, dapibus sed lectus. Donec rutrum augue ac dolor dapibus semper. Sed vitae efficitur neque, in tincidunt felis. Mauris non placerat sapien. Ut in ullamcorper ipsum. In quis cursus tellus, ut eleifend ipsum. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nullam tempor eget elit at imperdiet. Nam efficitur consectetur elit in dapibus. Vivamus nisi mi, viverra sed felis ac, eleifend tempor lectus. Morbi euismod pellentesque quam, non condimentum nulla ultricies a. Sed mollis risus at ante auctor, vel luctus enim iaculis. Nam tempus rutrum egestas. Nunc bibendum est sed arcu porttitor condimentum sit amet at lorem. Suspendisse massa orci, sollicitudin sit amet mauris tristique, vestibulum pellentesque augue."
console.log(text = encrypt(text, key))
console.log(decrypt(text,key))