let myPages = {
	main: {
		entry: 'src/pages/main/main.js',
		template: 'public/main.html',
		filename: 'main.html',
		title: 'main',
	},
	admin: {
		entry: 'src/pages/admin/main.js',
		template: 'public/admin.html',
		filename: 'admin.html',
		title: 'admin'
	},
	login: {
		entry: 'src/pages/login/main.js',
		template: 'public/login.html',
		filename: 'login.html',
		title: 'login'
	},
	index: {
		entry: 'src/pages/index/main.js',
		template: 'public/index.html',
		filename: 'index.html',
		title: 'index'
	}
}

module.exports = {
	configureWebpack: {
		devtool: 'source-map'
	},
	devServer:{
		port: 80,
	},
	chainWebpack: config => {
	},
	pages: myPages
}