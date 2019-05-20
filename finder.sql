-- phpMyAdmin SQL Dump
-- version 4.7.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 25-11-2017 a las 05:53:33
-- Versión del servidor: 10.1.28-MariaDB
-- Versión de PHP: 7.1.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `finder`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lote`
--

CREATE TABLE `lote` (
  `idLote` int(11) NOT NULL,
  `lote` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `fechaEntrada` date NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precioCompra` decimal(6,2) NOT NULL,
  `precioVenta` decimal(6,2) NOT NULL,
  `idProducto` int(11) NOT NULL,
  `idSucursal` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `lote`
--

INSERT INTO `lote` (`idLote`, `lote`, `fechaEntrada`, `cantidad`, `precioCompra`, `precioVenta`, `idProducto`, `idSucursal`) VALUES
(1, 'lote 1', '0000-00-00', 100, '8.00', '12.50', 1, 1),
(4, 'lote 2', '0000-00-00', 10, '2.35', '4.00', 4, 2),
(5, 'lote 3', '0000-00-00', 80, '1.90', '11.45', 4, 2),
(6, 'lote 4', '0000-00-00', 90, '80.12', '120.45', 5, 5),
(7, 'lote 5', '0000-00-00', 200, '77.00', '111.00', 2, 5),
(8, 'lote 6', '0000-00-00', 500, '90.00', '120.00', 3, 3),
(9, 'lote 7', '0000-00-00', 120, '7.50', '12.00', 1, 3),
(10, 'lote 8', '0000-00-00', 40, '8.30', '14.00', 1, 6),
(11, 'lote 9', '0000-00-00', 60, '19.00', '22.00', 2, 1),
(21, 'lote 10', '0000-00-00', 75, '12.00', '15.00', 2, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `idProducto` int(11) NOT NULL,
  `producto` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `descrip` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `idProveedor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`idProducto`, `producto`, `descrip`, `idProveedor`) VALUES
(1, 'Fajas', '-', 1),
(2, 'Tornillos', '-', 1),
(3, 'Bujías', '-', 1),
(4, 'Frenos', '-', 2),
(5, 'Desarmadores', '-', 2),
(6, 'Clutch', '-', 2),
(7, 'Pistones', '-', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedor`
--

CREATE TABLE `proveedor` (
  `idProveedor` int(11) NOT NULL,
  `proveedor` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `dir` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `tel` varchar(10) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `proveedor`
--

INSERT INTO `proveedor` (`idProveedor`, `proveedor`, `dir`, `tel`) VALUES
(1, 'Toyota', 'San Salvador', '2440-1090'),
(2, 'Chevrolet', 'Santa Ana', '2441-1242');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sucursal`
--

CREATE TABLE `sucursal` (
  `idSucursal` int(11) NOT NULL,
  `sucursal` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `dir` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `tel` varchar(10) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `correo` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `clave` varchar(15) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `lat` float NOT NULL,
  `lon` float NOT NULL,
  `activado` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `sucursal`
--

INSERT INTO `sucursal` (`idSucursal`, `sucursal`, `dir`, `tel`, `correo`, `clave`, `lat`, `lon`, `activado`) VALUES
(1, 'Sucursal 1', '21 Calle Pte., Santa Ana', '2440-0000', 'usuario1@gmail.com', '123456', 13.985, -89.5626, b'1'),
(2, 'Sucursal 2', 'Diagonal David Granadino, Santa Ana', '2440-0000', 'usuario2@gmail.com', '123456', 13.9871, -89.5475, b'1'),
(3, 'Sucursal 3', 'Calle Dr. Jos? Guerrero, Santa Ana', '2440-0000', 'usuario3@gmail.com', '123456', 13.9794, -89.5557, b'1'),
(5, 'Sucursal 4', '25 Calle Pte., Santa Ana', '2440-0000', 'usuario4@gmail.com', '123456', 13.9837, -89.5646, b'1'),
(6, 'Sucursal 5', 'Ave Fray Felipe De Jesus Moraga Sur, Santa Ana', '2440-0000', 'usuario5@gmail.com', '123456', 13.9771, -89.5701, b'0');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `lote`
--
ALTER TABLE `lote`
  ADD PRIMARY KEY (`idLote`),
  ADD UNIQUE KEY `lote` (`lote`),
  ADD KEY `idProducto` (`idProducto`),
  ADD KEY `idSucursal` (`idSucursal`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`idProducto`),
  ADD UNIQUE KEY `producto` (`producto`),
  ADD KEY `idProveedor` (`idProveedor`);

--
-- Indices de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  ADD PRIMARY KEY (`idProveedor`),
  ADD UNIQUE KEY `proveedor` (`proveedor`);

--
-- Indices de la tabla `sucursal`
--
ALTER TABLE `sucursal`
  ADD PRIMARY KEY (`idSucursal`),
  ADD UNIQUE KEY `sucursal` (`sucursal`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `lote`
--
ALTER TABLE `lote`
  MODIFY `idLote` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `idProducto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  MODIFY `idProveedor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `sucursal`
--
ALTER TABLE `sucursal`
  MODIFY `idSucursal` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `lote`
--
ALTER TABLE `lote`
  ADD CONSTRAINT `lote_ibfk_1` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`),
  ADD CONSTRAINT `lote_ibfk_2` FOREIGN KEY (`idSucursal`) REFERENCES `sucursal` (`idSucursal`);

--
-- Filtros para la tabla `producto`
--
ALTER TABLE `producto`
  ADD CONSTRAINT `producto_ibfk_1` FOREIGN KEY (`idProveedor`) REFERENCES `proveedor` (`idProveedor`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
