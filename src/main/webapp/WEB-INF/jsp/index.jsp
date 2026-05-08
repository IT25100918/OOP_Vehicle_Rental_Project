<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Elite Wheel Rentals — Find &amp; Rent the Perfect Vehicle</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,700;0,900;1,700&family=DM+Sans:wght@300;400;500;600&family=Bebas+Neue&display=swap" rel="stylesheet">
<style>
  *, *::before, *::after { margin: 0; padding: 0; box-sizing: border-box; }
  :root { --gold: #D4A843; --gold-light: #F0C96A; --dark: #0D0D0D; --navy: #0A1628; --blue: #1A6EFF; --white: #FFFFFF; --gray: #8A8A8A; --gray-2: #B0B0B0; }
  html { scroll-behavior: smooth; }
  body { font-family: 'DM Sans', sans-serif; background: var(--dark); color: var(--white); overflow-x: hidden; }
  nav { position: fixed; top: 0; left: 0; right: 0; z-index: 100; display: flex; align-items: center; justify-content: space-between; padding: 20px 60px; background: rgba(13,13,13,0.88); backdrop-filter: blur(20px); border-bottom: 1px solid rgba(255,255,255,0.06); transition: box-shadow 0.3s; }
  .nav-logo { display: flex; align-items: center; gap: 10px; text-decoration: none; }
  .nav-logo-img { height: 60px; width: auto; object-fit: contain; filter: drop-shadow(0 2px 12px rgba(212,168,67,0.35)); }
  .nav-links { display: flex; align-items: center; gap: 36px; list-style: none; }
  .nav-links a { color: var(--gray-2); font-size: 14px; font-weight: 500; text-decoration: none; transition: color 0.2s; }
  .nav-links a:hover { color: var(--white); }
  .nav-actions { display: flex; align-items: center; gap: 12px; }
  .btn-ghost { padding: 9px 22px; border-radius: 8px; font-size: 14px; font-weight: 500; border: 1px solid rgba(255,255,255,0.15); color: var(--white); background: transparent; text-decoration: none; transition: all 0.2s; display: inline-block; }
  .btn-ghost:hover { border-color: var(--gold); color: var(--gold); }
  .btn-primary { padding: 10px 24px; border-radius: 8px; font-size: 14px; font-weight: 600; background: var(--gold); color: var(--dark); border: none; text-decoration: none; display: inline-block; transition: all 0.2s; }
  .btn-primary:hover { background: var(--gold-light); transform: translateY(-1px); }
  .nav-user-btn { display: flex; align-items: center; gap: 8px; padding: 8px 18px; border-radius: 8px; background: rgba(212,168,67,0.1); border: 1px solid rgba(212,168,67,0.25); color: var(--gold); font-size: 14px; font-weight: 500; text-decoration: none; transition: all 0.2s; }
  .nav-user-btn:hover { background: rgba(212,168,67,0.18); }
  .hero { min-height: 100vh; position: relative; display: flex; flex-direction: column; justify-content: center; padding: 120px 60px 80px; overflow: hidden; }
  .hero-bg { position: absolute; inset: 0; z-index: 0; background: radial-gradient(ellipse 80% 60% at 70% 50%, rgba(26,110,255,0.12) 0%, transparent 60%), radial-gradient(ellipse 50% 70% at 20% 80%, rgba(212,168,67,0.08) 0%, transparent 50%), linear-gradient(160deg, #0D0D0D 0%, #0F1F3D 60%, #0D0D0D 100%); }
  .hero-grid-overlay { position: absolute; inset: 0; z-index: 0; background-image: linear-gradient(rgba(255,255,255,0.025) 1px, transparent 1px), linear-gradient(90deg, rgba(255,255,255,0.025) 1px, transparent 1px); background-size: 60px 60px; mask-image: radial-gradient(ellipse 80% 80% at 50% 50%, black 20%, transparent 80%); }
  .glow-orb { position: absolute; border-radius: 50%; filter: blur(80px); pointer-events: none; }
  .orb-1 { width: 400px; height: 400px; background: rgba(26,110,255,0.12); top: 10%; right: 5%; }
  .orb-2 { width: 300px; height: 300px; background: rgba(212,168,67,0.08); bottom: 20%; right: 25%; }
  .hero-badge { display: inline-flex; align-items: center; gap: 8px; padding: 6px 16px; border-radius: 999px; background: rgba(212,168,67,0.1); border: 1px solid rgba(212,168,67,0.3); font-size: 11px; font-weight: 600; color: var(--gold); letter-spacing: 2px; text-transform: uppercase; margin-bottom: 32px; position: relative; z-index: 1; animation: fadeInUp 0.6s ease both; }
  .hero-badge::before { content: ''; width: 6px; height: 6px; border-radius: 50%; background: var(--gold); animation: pulse 2s infinite; }
  @keyframes pulse { 0%, 100% { opacity: 1; transform: scale(1); } 50% { opacity: 0.5; transform: scale(1.3); } }
  .hero-headline { position: relative; z-index: 1; font-family: 'Playfair Display', serif; font-size: clamp(56px, 7vw, 100px); font-weight: 900; line-height: 1.0; color: var(--white); max-width: 780px; margin-bottom: 28px; animation: fadeInUp 0.7s ease 0.1s both; }
  .hero-headline em { font-style: italic; color: var(--gold); }
  .hero-sub { position: relative; z-index: 1; font-size: 17px; color: var(--gray-2); line-height: 1.7; max-width: 460px; margin-bottom: 48px; animation: fadeInUp 0.7s ease 0.2s both; }
  .hero-actions { display: flex; align-items: center; gap: 16px; position: relative; z-index: 1; margin-bottom: 72px; animation: fadeInUp 0.7s ease 0.3s both; }
  .btn-hero-primary { display: inline-flex; align-items: center; gap: 10px; padding: 16px 36px; border-radius: 12px; font-size: 15px; font-weight: 600; background: linear-gradient(135deg, var(--gold), #E8B84B); color: var(--dark); border: none; text-decoration: none; transition: all 0.25s; box-shadow: 0 8px 32px rgba(212,168,67,0.3); }
  .btn-hero-primary:hover { transform: translateY(-2px); box-shadow: 0 12px 40px rgba(212,168,67,0.4); }
  .btn-hero-ghost { display: inline-flex; align-items: center; gap: 10px; padding: 15px 32px; border-radius: 12px; font-size: 15px; font-weight: 500; background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.12); color: var(--white); text-decoration: none; transition: all 0.25s; }
  .btn-hero-ghost:hover { background: rgba(255,255,255,0.1); border-color: rgba(255,255,255,0.25); }
  .hero-stats { display: flex; gap: 48px; align-items: center; position: relative; z-index: 1; padding-top: 40px; border-top: 1px solid rgba(255,255,255,0.08); animation: fadeInUp 0.7s ease 0.4s both; }
  .hero-stat-num { font-family: 'Bebas Neue', sans-serif; font-size: 42px; color: var(--white); line-height: 1; letter-spacing: 1px; }
  .hero-stat-num span { color: var(--gold); }
  .hero-stat-label { font-size: 11px; text-transform: uppercase; letter-spacing: 2px; color: var(--gray); margin-top: 4px; font-weight: 500; }
  .stat-divider { width: 1px; height: 48px; background: rgba(255,255,255,0.1); }
  .search-section { padding: 0 60px; margin-top: -30px; position: relative; z-index: 10; }
  .search-bar { background: rgba(20,20,20,0.96); border: 1px solid rgba(255,255,255,0.1); border-radius: 20px; padding: 24px 28px; display: flex; align-items: flex-end; gap: 20px; backdrop-filter: blur(30px); box-shadow: 0 20px 60px rgba(0,0,0,0.4); max-width: 900px; }
  .search-field { flex: 1; }
  .search-label { font-size: 11px; text-transform: uppercase; letter-spacing: 1.5px; color: var(--gray); font-weight: 600; margin-bottom: 8px; }
  .search-input { width: 100%; background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.1); border-radius: 10px; padding: 12px 16px; font-size: 14px; color: var(--white); font-family: 'DM Sans', sans-serif; outline: none; transition: border-color 0.2s; }
  .search-input option { background: #1C1C1C; }
  .search-input:focus { border-color: var(--gold); }
  .search-btn { padding: 13px 32px; border-radius: 10px; font-size: 14px; font-weight: 600; background: var(--gold); color: var(--dark); border: none; cursor: pointer; white-space: nowrap; transition: all 0.2s; font-family: 'DM Sans', sans-serif; }
  .search-btn:hover { background: var(--gold-light); transform: translateY(-1px); }
  .section-eyebrow { font-size: 11px; text-transform: uppercase; letter-spacing: 3px; color: var(--gold); font-weight: 600; margin-bottom: 16px; }
  .section-title { font-family: 'Playfair Display', serif; font-size: clamp(36px, 4vw, 52px); font-weight: 700; line-height: 1.15; margin-bottom: 16px; }
  .section-title em { font-style: italic; color: var(--gold); }
  .section-sub { color: var(--gray-2); font-size: 16px; line-height: 1.7; max-width: 480px; margin-bottom: 56px; }
  .categories-section { padding: 100px 60px 60px; }
  .category-grid { display: grid; grid-template-columns: repeat(7, 1fr); gap: 14px; }
  .category-card { background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.07); border-radius: 16px; padding: 28px 20px; text-align: center; text-decoration: none; color: var(--white); position: relative; overflow: hidden; transition: all 0.3s; }
  .category-card:hover { border-color: rgba(212,168,67,0.4); transform: translateY(-4px); }
  .category-icon { font-size: 36px; margin-bottom: 14px; display: block; }
  .category-name { font-size: 14px; font-weight: 600; }
  .category-count { font-size: 12px; color: var(--gray); margin-top: 4px; }
  .vehicles-section { padding: 60px 60px 100px; }
  .section-header { display: flex; align-items: flex-end; justify-content: space-between; margin-bottom: 48px; }
  .view-all { font-size: 14px; font-weight: 500; color: var(--gold); text-decoration: none; display: flex; align-items: center; gap: 6px; transition: gap 0.2s; }
  .view-all:hover { gap: 10px; }
  .vehicles-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 24px; }
  .vehicle-card { background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.07); border-radius: 20px; overflow: hidden; transition: all 0.3s; }
  .vehicle-card:hover { border-color: rgba(212,168,67,0.3); transform: translateY(-6px); box-shadow: 0 20px 60px rgba(0,0,0,0.4); }
  .vehicle-img-wrap { height: 200px; position: relative; overflow: hidden; background: linear-gradient(135deg, #0F1F3D, #1A3A6B); display: flex; align-items: center; justify-content: center; font-size: 80px; }
  .vehicle-img-wrap img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.4s; position: absolute; inset: 0; }
  .vehicle-card:hover .vehicle-img-wrap img { transform: scale(1.05); }
  .vehicle-badge { position: absolute; top: 14px; left: 14px; padding: 4px 10px; border-radius: 999px; font-size: 10px; font-weight: 700; text-transform: uppercase; letter-spacing: 1px; }
  .badge-available { background: rgba(34,197,94,0.15); color: #4ADE80; border: 1px solid rgba(74,222,128,0.3); }
  .badge-rented { background: rgba(239,68,68,0.15); color: #F87171; border: 1px solid rgba(248,113,113,0.3); }
  .vehicle-body { padding: 22px; }
  .vehicle-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 14px; }
  .vehicle-name { font-family: 'Playfair Display', serif; font-size: 20px; font-weight: 700; }
  .vehicle-type-label { font-size: 11px; color: var(--gray); text-transform: uppercase; letter-spacing: 1px; margin-top: 2px; }
  .price-big { font-family: 'Bebas Neue', sans-serif; font-size: 28px; color: var(--gold); line-height: 1; text-align: right; }
  .price-per { font-size: 11px; color: var(--gray); text-align: right; }
  .vehicle-footer { display: flex; gap: 10px; margin-top: 18px; }
  .btn-book { flex: 1; padding: 11px; border-radius: 10px; font-size: 13px; font-weight: 600; background: var(--gold); color: var(--dark); border: none; cursor: pointer; transition: all 0.2s; text-decoration: none; text-align: center; display: block; }
  .btn-book:hover { background: var(--gold-light); }
  .btn-book-disabled { flex: 1; padding: 11px; border-radius: 10px; font-size: 13px; font-weight: 600; background: rgba(255,255,255,0.06); color: var(--gray); border: none; text-align: center; display: block; }
  .how-section { padding: 100px 60px; background: rgba(255,255,255,0.015); border-top: 1px solid rgba(255,255,255,0.05); border-bottom: 1px solid rgba(255,255,255,0.05); }
  .how-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 40px; margin-top: 60px; }
  .step-num { font-family: 'Bebas Neue', sans-serif; font-size: 72px; color: rgba(212,168,67,0.1); line-height: 1; margin-bottom: -10px; }
  .step-icon { width: 52px; height: 52px; border-radius: 14px; background: rgba(212,168,67,0.1); border: 1px solid rgba(212,168,67,0.2); display: flex; align-items: center; justify-content: center; font-size: 22px; margin-bottom: 18px; }
  .step-title { font-size: 17px; font-weight: 600; margin-bottom: 10px; }
  .step-desc { font-size: 14px; color: var(--gray-2); line-height: 1.7; }
  .reviews-section { padding: 100px 60px; }
  .reviews-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 24px; margin-top: 60px; }
  .review-card { background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.07); border-radius: 20px; padding: 28px; transition: border-color 0.3s; }
  .review-card:hover { border-color: rgba(212,168,67,0.2); }
  .review-stars { color: var(--gold); font-size: 16px; margin-bottom: 16px; }
  .review-text { font-size: 15px; color: var(--gray-2); line-height: 1.7; margin-bottom: 20px; font-style: italic; }
  .review-author { display: flex; align-items: center; gap: 12px; }
  .author-avatar { width: 40px; height: 40px; border-radius: 50%; background: linear-gradient(135deg, var(--gold), var(--blue)); display: flex; align-items: center; justify-content: center; font-size: 15px; font-weight: 700; color: var(--dark); flex-shrink: 0; }
  .author-name { font-size: 14px; font-weight: 600; }
  .author-meta { font-size: 12px; color: var(--gray); }
  footer { background: var(--navy); border-top: 1px solid rgba(255,255,255,0.06); padding: 80px 60px 0; }
  .footer-top { display: grid; grid-template-columns: 280px 1fr; gap: 80px; padding-bottom: 60px; border-bottom: 1px solid rgba(255,255,255,0.06); }
  .footer-tagline { font-size: 14px; color: var(--gray); line-height: 1.7; margin-bottom: 28px; }
  .footer-links-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 40px; }
  .footer-links-group h4 { font-size: 11px; text-transform: uppercase; letter-spacing: 2px; color: var(--white); font-weight: 700; margin-bottom: 20px; }
  .footer-links-group ul { list-style: none; display: flex; flex-direction: column; gap: 10px; }
  .footer-links-group a { font-size: 13px; color: var(--gray); text-decoration: none; transition: color 0.2s; }
  .footer-links-group a:hover { color: var(--gold); }
  .footer-bottom { display: flex; align-items: center; justify-content: space-between; padding: 24px 0; font-size: 13px; color: var(--gray); }
  .footer-legal { display: flex; gap: 24px; }
  .footer-legal a { color: var(--gray); text-decoration: none; }
  .back-to-top { display: flex; align-items: center; gap: 8px; background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.1); padding: 8px 16px; border-radius: 8px; font-size: 12px; font-weight: 600; color: var(--white); text-decoration: none; }
  .back-to-top:hover { background: rgba(212,168,67,0.15); color: var(--gold); }
  .animate-on-scroll { opacity: 0; transform: translateY(30px); transition: opacity 0.7s ease, transform 0.7s ease; }
  .animate-on-scroll.visible { opacity: 1; transform: translateY(0); }
  @keyframes fadeInUp { from { opacity: 0; transform: translateY(30px); } to { opacity: 1; transform: translateY(0); } }
</style>
</head>
<body>

<!-- NAV -->
<nav id="navbar">
  <a href="${pageContext.request.contextPath}/" class="nav-logo">
    <img src="${pageContext.request.contextPath}/logo.png" alt="Elite Wheel Rentals" class="nav-logo-img">
  </a>
  <ul class="nav-links">
    <li><a href="${pageContext.request.contextPath}/vehicles">Vehicles</a></li>
    <li><a href="${pageContext.request.contextPath}/feedback">Reviews</a></li>
    <c:if test="${not empty user and user.userType == 'admin'}">
      <li><a href="${pageContext.request.contextPath}/dashboard">Admin</a></li>
    </c:if>
    <c:if test="${not empty user}">
      <li><a href="${pageContext.request.contextPath}/bookings">My Bookings</a></li>
    </c:if>
  </ul>
  <div class="nav-actions">
    <c:if test="${empty user}">
      <a href="${pageContext.request.contextPath}/login" class="btn-ghost">Sign In</a>
      <a href="${pageContext.request.contextPath}/register" class="btn-primary" style="margin-left:10px">Get Started &rarr;</a>
    </c:if>
    <c:if test="${not empty user}">
      <a href="${pageContext.request.contextPath}/dashboard" class="nav-user-btn">&#x1F464; ${user.name}</a>
      <a href="${pageContext.request.contextPath}/logout" class="btn-ghost">Logout</a>
    </c:if>
  </div>
</nav>

<!-- HERO -->
<section class="hero" id="top">
  <div class="hero-bg"></div>
  <div class="hero-grid-overlay"></div>
  <div class="glow-orb orb-1"></div>
  <div class="glow-orb orb-2"></div>
  <div class="hero-badge">&#x25CF; Elite Wheel Rentals &mdash; Sri Lanka</div>
  <h1 class="hero-headline">Luxury<br><em>in Motion</em></h1>
  <p class="hero-sub">
    <em style="font-family:'Playfair Display',serif;font-size:22px;color:var(--gold);font-style:italic;display:block;margin-bottom:12px">Drive Your Freedom</em>
    From whisper-quiet sedans to roaring SUVs &mdash; browse, book, and hit the road. Every journey, elevated.
  </p>
  <div class="hero-actions">
    <a href="${pageContext.request.contextPath}/vehicles" class="btn-hero-primary">&#x1F697; Browse Fleet &rarr;</a>
    <c:if test="${empty user}">
      <a href="${pageContext.request.contextPath}/register" class="btn-hero-ghost">Get Started Free</a>
    </c:if>
    <c:if test="${not empty user}">
      <a href="${pageContext.request.contextPath}/dashboard" class="btn-hero-ghost">&#x25B6; My Dashboard</a>
    </c:if>
  </div>
  <div class="hero-stats">
    <div><div class="hero-stat-num">30<span>+</span></div><div class="hero-stat-label">Vehicles</div></div>
    <div class="stat-divider"></div>
    <div><div class="hero-stat-num">10<span>+</span></div><div class="hero-stat-label">Brands</div></div>
    <div class="stat-divider"></div>
    <div><div class="hero-stat-num">24<span>/7</span></div><div class="hero-stat-label">Support</div></div>
    <div class="stat-divider"></div>
    <div><div class="hero-stat-num"><fmt:formatNumber value="${avgRating}" pattern="#.0"/><span>&#9733;</span></div><div class="hero-stat-label">Rating</div></div>
  </div>
</section>

<!-- SEARCH BAR -->
<section class="search-section">
  <form class="search-bar" action="${pageContext.request.contextPath}/vehicles" method="GET">
    <div class="search-field">
      <div class="search-label">Vehicle Type</div>
      <select class="search-input" name="type">
        <option value="">All Types</option>
        <option value="car">&#x1F697; Car</option>
        <option value="suv">&#x1F699; SUV</option>
        <option value="van">&#x1F690; Van</option>
        <option value="bike">&#x1F3CD;&#xFE0F; Bike</option>
        <option value="threewheeler">&#x1F6FA; Three-Wheeler</option>
        <option value="bus">&#x1F68C; Bus</option>
        <option value="lorry">&#x1F69B; Lorry</option>
      </select>
    </div>
    <div class="search-field">
      <div class="search-label">Sort By</div>
      <select class="search-input" name="sortBy">
        <option value="availability">Availability First</option>
        <option value="price">Price: Low to High</option>
        <option value="type">Vehicle Type</option>
      </select>
    </div>
    <div class="search-field">
      <div class="search-label">Pick-up Date</div>
      <input type="date" class="search-input" name="startDate">
    </div>
    <div class="search-field">
      <div class="search-label">Return Date</div>
      <input type="date" class="search-input" name="endDate">
    </div>
    <button type="submit" class="search-btn">Search &rarr;</button>
  </form>
</section>

<!-- CATEGORIES -->
<section class="categories-section">
  <div class="animate-on-scroll">
    <div class="section-eyebrow">&#x2736; Browse by Category</div>
    <h2 class="section-title">Find Your Perfect <em>Ride</em></h2>
    <p class="section-sub">Explore our curated fleet &mdash; from budget-friendly commuters to premium luxury vehicles.</p>
  </div>
  <div class="category-grid animate-on-scroll">
    <a href="${pageContext.request.contextPath}/vehicles?type=car" class="category-card"><span class="category-icon">&#x1F697;</span><div class="category-name">Cars</div><div class="category-count">Most Popular</div></a>
    <a href="${pageContext.request.contextPath}/vehicles?type=suv" class="category-card"><span class="category-icon">&#x1F699;</span><div class="category-name">SUVs</div><div class="category-count">Off-Road Ready</div></a>
    <a href="${pageContext.request.contextPath}/vehicles?type=van" class="category-card"><span class="category-icon">&#x1F690;</span><div class="category-name">Vans</div><div class="category-count">Group Travel</div></a>
    <a href="${pageContext.request.contextPath}/vehicles?type=bike" class="category-card"><span class="category-icon">&#x1F3CD;</span><div class="category-name">Bikes</div><div class="category-count">Quick &amp; Agile</div></a>
    <a href="${pageContext.request.contextPath}/vehicles?type=threewheeler" class="category-card"><span class="category-icon">&#x1F6FA;</span><div class="category-name">Three-Wheelers</div><div class="category-count">City Rides</div></a>
    <a href="${pageContext.request.contextPath}/vehicles?type=bus" class="category-card"><span class="category-icon">&#x1F68C;</span><div class="category-name">Buses</div><div class="category-count">Large Groups</div></a>
    <a href="${pageContext.request.contextPath}/vehicles?type=lorry" class="category-card"><span class="category-icon">&#x1F69B;</span><div class="category-name">Lorries</div><div class="category-count">Heavy Cargo</div></a>
  </div>
</section>

<!-- FEATURED VEHICLES -->
<section class="vehicles-section" id="vehicles">
  <div class="section-header animate-on-scroll">
    <div>
      <div class="section-eyebrow">&#x2736; Available Now</div>
      <h2 class="section-title" style="margin-bottom:0">Featured <em>Fleet</em></h2>
    </div>
    <a href="${pageContext.request.contextPath}/vehicles" class="view-all">View All Vehicles &rarr;</a>
  </div>
  <div class="vehicles-grid">
    <c:forEach var="vehicle" items="${vehicles}" varStatus="vs">
      <c:if test="${vs.index < 6}">
        <div class="vehicle-card animate-on-scroll">
          <div class="vehicle-img-wrap">
            <span>&#x1F697;</span>
            <img src="${vehicle.imageUrl}" alt="${vehicle.brand} ${vehicle.model}" onerror="this.remove()">
            <c:if test="${vehicle.available}"><span class="vehicle-badge badge-available">Available</span></c:if>
            <c:if test="${!vehicle.available}"><span class="vehicle-badge badge-rented">Rented</span></c:if>
          </div>
          <div class="vehicle-body">
            <div class="vehicle-header">
              <div>
                <div class="vehicle-name">${vehicle.brand} ${vehicle.model}</div>
                <div class="vehicle-type-label">${vehicle.type}</div>
              </div>
              <div>
                <div class="price-big"><fmt:formatNumber value="${vehicle.rentPrice}" pattern="#,##0"/></div>
                <div class="price-per">Rs/day</div>
              </div>
            </div>
            <div class="vehicle-footer">
              <c:if test="${vehicle.available and not empty user}">
                <a href="${pageContext.request.contextPath}/bookings/create/${vehicle.vehicleId}" class="btn-book">Book Now</a>
              </c:if>
              <c:if test="${vehicle.available and empty user}">
                <a href="${pageContext.request.contextPath}/login" class="btn-book">Book Now</a>
              </c:if>
              <c:if test="${!vehicle.available}">
                <span class="btn-book-disabled">Not Available</span>
              </c:if>
            </div>
          </div>
        </div>
      </c:if>
    </c:forEach>
  </div>
</section>

<!-- HOW IT WORKS -->
<section class="how-section" id="how">
  <div class="animate-on-scroll" style="text-align:center;max-width:600px;margin:0 auto">
    <div class="section-eyebrow">&#x2736; Simple Process</div>
    <h2 class="section-title">Book in <em>4 Easy Steps</em></h2>
  </div>
  <div class="how-grid">
    <div class="how-step animate-on-scroll"><div class="step-num">01</div><div class="step-icon">&#x1F50D;</div><div class="step-title">Browse Fleet</div><p class="step-desc">Explore our full catalogue of cars, vans, and bikes. Filter by type, price, or availability.</p></div>
    <div class="how-step animate-on-scroll" style="transition-delay:0.1s"><div class="step-num">02</div><div class="step-icon">&#x1F4C5;</div><div class="step-title">Select Dates</div><p class="step-desc">Choose your pick-up and return dates. Total cost is calculated instantly.</p></div>
    <div class="how-step animate-on-scroll" style="transition-delay:0.2s"><div class="step-num">03</div><div class="step-icon">&#x2705;</div><div class="step-title">Confirm Booking</div><p class="step-desc">Review your booking details and confirm. Your vehicle is reserved immediately.</p></div>
    <div class="how-step animate-on-scroll" style="transition-delay:0.3s"><div class="step-num">04</div><div class="step-icon">&#x1F4B3;</div><div class="step-title">Pay &amp; Drive</div><p class="step-desc">Complete payment via cash or online. Pick up your vehicle and hit the road.</p></div>
  </div>
</section>

<!-- REVIEWS -->
<c:if test="${not empty reviews}">
<section class="reviews-section" id="reviews">
  <div class="animate-on-scroll" style="text-align:center;max-width:600px;margin:0 auto">
    <div class="section-eyebrow">&#x2736; Customer Feedback</div>
    <h2 class="section-title">What Our Customers <em>Say</em></h2>
  </div>
  <div class="reviews-grid">
    <c:forEach var="review" items="${reviews}" varStatus="rs">
      <c:if test="${rs.index < 3}">
        <div class="review-card animate-on-scroll">
          <div class="review-stars">${review.stars}</div>
          <p class="review-text">"${review.comment}"</p>
          <div class="review-author">
            <div class="author-avatar">${review.userName.substring(0,1)}</div>
            <div>
              <div class="author-name">${review.userName}</div>
              <div class="author-meta">${review.vehicleInfo}</div>
            </div>
          </div>
        </div>
      </c:if>
    </c:forEach>
  </div>
</section>
</c:if>

<!-- FOOTER -->
<footer>
  <div class="footer-top">
    <div>
      <div style="margin-bottom:14px">
        <img src="${pageContext.request.contextPath}/logo.png" alt="Elite Wheel Rentals" style="height:80px;width:auto;object-fit:contain;filter:drop-shadow(0 2px 12px rgba(212,168,67,0.35))">
      </div>
      <p class="footer-tagline"><em style="font-family:'Georgia',serif;color:var(--gold);font-style:italic">"Drive Your Freedom"</em><br>Sri Lanka's premier vehicle rental platform. Every journey, elevated.</p>
    </div>
    <div class="footer-links-grid">
      <div class="footer-links-group"><h4>Fleet</h4><ul><li><a href="${pageContext.request.contextPath}/vehicles?type=car">Cars</a></li><li><a href="${pageContext.request.contextPath}/vehicles?type=van">Vans</a></li><li><a href="${pageContext.request.contextPath}/vehicles?type=bike">Bikes</a></li><li><a href="${pageContext.request.contextPath}/vehicles">All Vehicles</a></li></ul></div>
      <div class="footer-links-group"><h4>Account</h4><ul><li><a href="${pageContext.request.contextPath}/register">Register</a></li><li><a href="${pageContext.request.contextPath}/login">Sign In</a></li><li><a href="${pageContext.request.contextPath}/users/profile">My Profile</a></li><li><a href="${pageContext.request.contextPath}/bookings">My Bookings</a></li></ul></div>
      <div class="footer-links-group"><h4>Services</h4><ul><li><a href="${pageContext.request.contextPath}/bookings">Online Booking</a></li><li><a href="${pageContext.request.contextPath}/payments">Payments</a></li><li><a href="${pageContext.request.contextPath}/feedback">Reviews</a></li><li><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li></ul></div>
      <div class="footer-links-group"><h4>Company</h4><ul><li><a href="#">About Us</a></li><li><a href="#">Contact</a></li><li><a href="${pageContext.request.contextPath}/dashboard">Admin Portal</a></li></ul></div>
    </div>
  </div>
  <div class="footer-bottom">
    <div>&copy;2026 Elite Wheel Rentals &middot; SE1020 OOP Project</div>
    <div class="footer-legal"><a href="#">Privacy Policy</a><a href="#">Terms of Use</a></div>
    <a href="#top" class="back-to-top">BACK TO TOP &#x2191;</a>
  </div>
</footer>

<script>
  const observer = new IntersectionObserver(entries => {
    entries.forEach(el => { if (el.isIntersecting) el.target.classList.add('visible'); });
  }, { threshold: 0.1 });
  document.querySelectorAll('.animate-on-scroll').forEach(el => observer.observe(el));
  window.addEventListener('scroll', () => {
    document.getElementById('navbar').style.boxShadow = window.scrollY > 10 ? '0 4px 40px rgba(0,0,0,0.5)' : 'none';
  });
  const today = new Date().toISOString().split('T')[0];
  document.querySelectorAll('input[type="date"]').forEach(el => el.min = today);
</script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
